package com.girildo.programminoAPI;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.flickr4java.flickr.FlickrException;
import com.girildo.programminoAPI.Commento.TipoCommento;

public class LogicaProgrammaCM extends LogicaProgramma {


	@Override
	public Messaggio GeneraClassifica(int numPreferenze) {
		// TODO Auto-generated method stub
		return null;
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
				throw new IllegalArgumentException("Il commento di "+c.getAutore().getNome()+" dovrebbe contenere una foto ma non trovo il cancelletto" );
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
					builder.append(s.replace(" ", "").trim());
				else if(s.matches("[TEOteo] ?:? ?(# ?\\d{1,2}) ?STOP *")) //per beccare le votazioni con stop
					builder.append(s.replace("STOP", "").replace(" ", "").trim());
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
