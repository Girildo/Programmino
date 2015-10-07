package com.girildo.programminoAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.groups.discuss.GroupDiscussInterface;
import com.flickr4java.flickr.groups.discuss.Reply;
import com.flickr4java.flickr.groups.discuss.ReplyObject;
import com.girildo.programminoAPI.Messaggio.FlagMessaggio;
import com.girildo.programminoAPI.LogicaProgramma;



public class LogicaProgrammaSG extends LogicaProgramma
{
	
	public LogicaProgrammaSG()
	{
		super();
	}
	
	ArrayList<Commento> listaCommenti;
	HashMap<Integer, Foto> dictionaryFoto;
	
	@Override
	protected String ottieniCommenti(String topicID) throws FlickrException
	{
		Flickr flickr = new Flickr(KEY, SECRET, new REST());
        GroupDiscussInterface dInterface = flickr.getDiscussionInterface();
        int count = dInterface.getTopicInfo(topicID).getCountReplies(); //count delle risposte
        ReplyObject rep = dInterface.getReplyList(topicID, count, 1); //ottiene l'oggetto dall'API
        ArrayList<Reply> repList = rep.getReplyList(); //estrae la lista delle risposte
        listaCommenti = new ArrayList<Commento>(); //lista di commenti con classe custom
        for(Reply reply : repList)
        {
        	Pattern p = Pattern.compile("<img class.+alt=\"Classifica\" />");
        	if(p.matcher(reply.getMessage()).find())
        	{
        		//System.out.println("qui");
        		break;
        	}
        	Commento commento = new Commento(reply.getMessage(), new Autore(reply.getAuthorname(), reply.getAuthorId()));
        	listaCommenti.add(commento);
        }
        StringBuilder builder = new StringBuilder();
        for(Commento c:listaCommenti)
        {
        	if(c.getTipo() == Commento.TipoCommento.FOTO)
        		builder.append(c.toString()+'\n');
        }
        return builder.toString();
	}

	List<Autore> listaAutoVoto, listaAutoriCheHannoVotato;
	
	@Override
	public Messaggio GeneraClassifica(int numPreferenze)
	{
		dictionaryFoto = new HashMap<Integer, Foto>();
		listaAutoVoto = new ArrayList<Autore>();
		listaAutoriCheHannoVotato = new ArrayList<Autore>();
		
		ArrayList<Commento> commentiConErrori = new ArrayList<Commento>(); //lista che contiene i commenti con meno preferenze dell'imp.
		for(Commento c:listaCommenti)
		{
			if(c.getTipo() == Commento.TipoCommento.STARTVOTING || c.getTipo() == Commento.TipoCommento.IGNORA)
				continue;
			if(c.getTipo() == Commento.TipoCommento.VOTAZIONE)
			{
				String[] split = c.getTesto().split("#", -1);
				if(split.length<numPreferenze+1) //Se ho meno di quante preferenze mi aspetto c'è un problema
				{
					commentiConErrori.add(c);
					return new Messaggio("Il voto di " + c.getAutore().getNome() + " sembra avere un problema col "+
					"numero delle preferenze", FlagMessaggio.ERRORE);
				}
				ArrayList<Foto> fotoVotateInQuestoCommento = new ArrayList<Foto>();
				for(int i=0; i<numPreferenze; i++) //itera tra gli id ottenuti con lo split 
				{
					int id = 0;
					Foto foto = null;
					try
					{
						id = Integer.parseInt(split[i+1]); //cast a int della stringa
						foto = dictionaryFoto.get(id); //ottiene la foto dall'hash set
						if(!fotoVotateInQuestoCommento.contains(foto))
						{
							foto.aumentaVoti(numPreferenze-i); //aumenta i voti della foto
							fotoVotateInQuestoCommento.add(foto);
						}
						else
						{
							commentiConErrori.add(c);
							return new Messaggio("Il commento di " + c.getAutore().getNome() + " contiene un doppio voto",
									FlagMessaggio.ERRORE);
						}
						
						if(!listaAutoriCheHannoVotato.contains(c.getAutore()))
							listaAutoriCheHannoVotato.add(c.getAutore());
						
						if(foto.getAutore().equals(c.getAutore())) //se l'autore della foto è lo stesso del commenot
						{
							listaAutoVoto.add(c.getAutore()); //c'è autovoto
						}
					}
					catch(Exception e)
					{
						System.out.println(id);
					}
				}
			}
			else //se non è votazione (=È foto)
			{
				Autore autore = c.getAutore();
				int id = Integer.parseInt(c.getTesto().replace("#", ""));
				Foto foto = new Foto(autore, id);
				autore.setSuaFoto(foto);
				dictionaryFoto.put(id, foto);
			}
		}
		List<Foto> listaOrdinataPerClassifica = new ArrayList<Foto>(dictionaryFoto.values());
		Collections.sort(listaOrdinataPerClassifica); //ordina la lista (dal più basso al più alto)
		Collections.reverse(listaOrdinataPerClassifica); //inverte l'ordine
		StringBuilder builderClassifica = new StringBuilder();
		StringBuilder builderNonVotanti = new StringBuilder("Non hanno votato: \n");
		StringBuilder builderAutoVoto = new StringBuilder("Si sono autovotati: \n");
		
		FlagMessaggio flagXMessaggio = FlagMessaggio.NESSUN_ERRORE;
		
		for(Foto f:listaOrdinataPerClassifica)
		{
			if(!listaAutoriCheHannoVotato.contains(f.getAutore()))
			{
				builderNonVotanti.append("• " + f.getAutore().getNomeAbbreviato()+
						" (#"+f.getID()+" con "+f.getVoti()+" punti)\n");
				flagXMessaggio = FlagMessaggio.ERRORE_PARZIALE;
			}
			if(listaAutoVoto.contains(f.getAutore()))
			{
				builderAutoVoto.append("• " + f.getAutore().getNomeAbbreviato()+"\n");
				flagXMessaggio = FlagMessaggio.ERRORE_PARZIALE;
			}
			builderClassifica.append(f.toString() + "\n");
		}
		builderNonVotanti.append("-----------------------\n");
		builderAutoVoto.append("-----------------------\n");
		builderClassifica.append("-----------------------\n");
		builderClassifica.append("Foto trovate: "+dictionaryFoto.size()+"\n");
		builderClassifica.append("Hanno votato in "+listaAutoriCheHannoVotato.size()+"\n");
		builderClassifica.append("Si sono autovotati in "+listaAutoVoto.size()+"\n");
		builderClassifica.append("Voti con errore: "+commentiConErrori.size()+"\n");
		
		if(listaAutoVoto.size() == 0)
			builderAutoVoto.delete(0, builderAutoVoto.length());
		
		
		if(Collections.max(listaOrdinataPerClassifica).getVoti() == 0)
			return new Messaggio("Sembra che tutte le foto abbiano 0 voti; controlla che il numero delle"
					+ " preferenze di voto sia giusto!", FlagMessaggio.ERRORE);
		
		return new Messaggio(builderClassifica.toString(), flagXMessaggio
				, builderNonVotanti.toString()+builderAutoVoto.toString());
	}
}
