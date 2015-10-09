package com.girildo.programminoAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.flickr4java.flickr.FlickrException;
import com.girildo.programminoAPI.Commento.TipoCommento;
import com.girildo.programminoAPI.Messaggio.FlagMessaggio;



public class LogicaProgrammaCM extends LogicaProgramma 
{
	private enum Categoria{TECNICA, ESPRESSIVITA, ORIGINALITA}
	
	@SuppressWarnings("unused")
	@Override
	public Messaggio GeneraClassifica(int numPreferenze) 
	{
		HashMap<Integer, Foto> dictionaryFoto = new HashMap<Integer, Foto>();
		HashMap<Integer, Foto> classificaGenerale = new HashMap<Integer, Foto>();
		HashMap<Integer, Foto> classificaTecn = new HashMap<Integer, Foto>();
		HashMap<Integer, Foto> classificaEspr = new HashMap<Integer, Foto>();
		HashMap<Integer, Foto> classificaOrig = new HashMap<Integer, Foto>();
		
		ArrayList<Autore> listaAutoriCheHannoVotato = new ArrayList<Autore>();
		ArrayList<Autore> listaAutoriConAutovoto = new ArrayList<Autore>();
		ArrayList<Commento> commentiConErrore = new ArrayList<Commento>();
		
		for(Commento c:listaCommenti)
		{
			ArrayList<Foto> votateNelCommentoTecn = new ArrayList<Foto>();
			ArrayList<Foto> votateNelCommentoEspr = new ArrayList<Foto>();
			ArrayList<Foto> votateNelCommentoOrig = new ArrayList<Foto>();
			
			if(c.getTipo() == TipoCommento.IGNORA)
				continue;
			
			else if (c.getTipo() == TipoCommento.STARTVOTING) //popola le classifiche con le foto
			{
				for(Foto f:dictionaryFoto.values())
				{
					classificaGenerale.put(f.getID(), f.clonaFoto());
					classificaTecn.put(f.getID(), f.clonaFoto());
					classificaEspr.put(f.getID(), f.clonaFoto());
					classificaOrig.put(f.getID(), f.clonaFoto());
				}
			}
			
			
			else if(c.getTipo() == TipoCommento.FOTO)
			{
				try
				{
					Foto foto = super.generaFotoDaCommento(c);
					dictionaryFoto.put(foto.getID(), foto);
				}
				catch (NumberFormatException ex)
				{
					return new Messaggio("Nel commento contenente la foto di " + c.getAutore().getNome()
							+ "ho trovato un errore nel numero", FlagMessaggio.ERRORE);
				}
			}
			
			
			else if(c.getTipo() == TipoCommento.VOTAZIONE)
			{
				String[] split = c.getTesto().split("!");
				if(split.length < 9)
				{
					return new Messaggio("La votazione di "+ c.getAutore().getNome() +
							" sembra avere meno di nove voti", FlagMessaggio.ERRORE);
				}
				
				for(String s:split)
				{
					if(!s.matches("[teoTEO] ?:? ?# ?\\d{1,2}"))
						return new Messaggio("La votazione di " + c.getAutore().getNome() + 
								" sembra avere un errore di formato", FlagMessaggio.ERRORE);
					else
					{
						String testo = s.replace(":", "").replace(" ", ""); //pulisce la stringa iterata da : e spazi
						char tipoVoto = Character.toUpperCase(testo.charAt(0)); //Salva per reference il primo carattere
						testo = testo.substring(2);	//rimuove il primo carattere e il cancelletto			
						int parseID = Integer.parseInt(testo);
						Foto fotoVotata = dictionaryFoto.get(parseID);
						
						if(dictionaryFoto.get(parseID).getAutore() == c.getAutore()) //check sull'autovoto
						{
							listaAutoriConAutovoto.add(c.getAutore());
						}
						
						classificaGenerale.get(parseID).aumentaVoti(1);
						switch (tipoVoto) //Definisce la classifica da recuperare
						{
							case 'T':
								if(votateNelCommentoTecn.contains(dictionaryFoto.get(parseID)))
									return new Messaggio(c.getAutore().getNome() + " ha votato due volte la stessa"
											+ " foto nella categoria 'tecnica'", FlagMessaggio.ERRORE);
								classificaTecn.get(parseID).aumentaVoti(1);
								votateNelCommentoTecn.add(dictionaryFoto.get(parseID));
								break;
							case 'E':
								if(votateNelCommentoEspr.contains(dictionaryFoto.get(parseID)))
									return new Messaggio(c.getAutore().getNome() + " ha votato due volte la stessa"
											+ " foto nella categoria 'espressività'", FlagMessaggio.ERRORE);
								classificaEspr.get(parseID).aumentaVoti(1);
								votateNelCommentoEspr.add(dictionaryFoto.get(parseID));
								break;
							case 'O':
								if(votateNelCommentoOrig.contains(dictionaryFoto.get(parseID)))
									return new Messaggio(c.getAutore().getNome() + " ha votato due volte la stessa"
											+ " foto nella categoria 'originalità'", FlagMessaggio.ERRORE);
								classificaOrig.get(parseID).aumentaVoti(1);
								votateNelCommentoOrig.add(dictionaryFoto.get(parseID));
								break;
						}	
					}
				}
			}
		}
		return new Messaggio(null, FlagMessaggio.ERRORE);
	}

	@Override
	protected boolean pulisciCommenti(ArrayList<Commento> commentiSporchi) 
	{
		super.listaCommenti = new ArrayList<Commento>();
		for(Commento c : commentiSporchi)
		{
			String testo = c.getTesto();
			if(!testo.contains("#") && testo.contains("<a href")) //se il commento contiene un link ad una foto ma niente cancelletto
			{													  //spara una exception catched dal chiamante
				throw new IllegalArgumentException("Il commento di "+c.getAutore().getNome()
						+ " dovrebbe contenere una foto ma non trovo il cancelletto" );
			}
			if(!testo.contains("#") && !testo.contains("<a href"))
			{
				c.setTipo(TipoCommento.IGNORA);
			}
			testo = testo.replaceAll("<a.+\\/><\\/a>", "");
			String[] split = testo.split("\n");
			StringBuilder builder = new StringBuilder(); 
			Pattern pattern = Pattern.compile("(# ?\\d{1,2})+");
			for(String s : split)
			{
				if(s.isEmpty())
					continue;
				
				if(s.matches("Esempio di votazione:"))			
				{
					Commento.Voting = true;
					c.setTipo(TipoCommento.STARTVOTING);
				}
				
				if(s.matches("[TEOteo] ?:? ?(# ?\\d{1,2})")) //per beccare le votazioni
					builder.append(s.replace(" ", "").trim()+"!");
				else if(s.matches("[TEOteo] ?:? ?(# ?\\d{1,2}) ?STOP *")) //per beccare le votazioni con stop
					builder.append(s.replace("STOP", "").replace(" ", "").trim()+"!");
				else //per beccare le foto
				{
					Matcher matcher = pattern.matcher(s);
					if(matcher.find())
						builder.append(matcher.group());
				}
			}
			c.setTesto(builder.toString()); //setta il testo di questo commento al builder
			c.AggiornaTipo(TipoLogica.LOGICA_CM); //aggiorna il tipo
			listaCommenti.add(c); //aggiorna la lista
			System.out.println(c.getTipo());
			if(c.getTipo() == TipoCommento.VOTAZIONE)
				System.out.println(c.getTesto());
		}
		return listaCommenti.size() == commentiSporchi.size();
	}

}
