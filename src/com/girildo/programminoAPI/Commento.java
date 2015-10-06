package com.girildo.programminoAPI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Commento
{
	private static boolean voting;
	public enum TipoCommento{FOTO, VOTAZIONE, STARTVOTING, IGNORA}
	private String testo;
	private Autore autore;
	private TipoCommento tipo;
	
	public Commento(String testo, Autore autore)
	{
		this.autore = autore;
		this.testo = pulisciTesto(testo);
		if(this.tipo != TipoCommento.STARTVOTING)
			this.tipo = voting?TipoCommento.VOTAZIONE:TipoCommento.FOTO;
	}
	
	public static void resetVotingFlag()
	{
		Commento.voting = false;
	}
	
	public TipoCommento getTipo()
	{
		return tipo;
	}

	private String pulisciTesto(String testo)
	{
		if(!testo.contains("#") && testo.contains("<a href")) //se il commento contiene un link ad una foto ma niente cancelletto
		{													  //spara una exception catched dal chiamante
			throw new IllegalArgumentException("Il commento di "+this.autore.getNome()+" dovrebbe contenere una foto ma non trovo il cancelletto" );
		}
		if(!testo.contains("#") && !testo.contains("<a href")) //se il commento non contiene né foto né cancelletto è da ignorare
		{
			this.tipo = TipoCommento.IGNORA;
		}
		testo = testo.replaceAll("<a.+\\/><\\/a>", "");
		String[] split = testo.split("\n");
		StringBuilder builder = new StringBuilder(); 
		Pattern pattern = Pattern.compile("(# ?\\d{1,2})+");
		for(String s : split)
		{
			if(s.isEmpty())
				continue;
			if(s.matches("#{6,100}"))			
			{
				Commento.voting = true;
				this.tipo = TipoCommento.STARTVOTING;
			}
			if(s.matches("(# ?\\d{1,2})+"))
				builder.append(s);
			else if(s.matches("(# ?\\d{1,2}) ?STOP *"))
				builder.append(s.replace("STOP", "").trim());
			else
			{
				Matcher matcher = pattern.matcher(s);
				if(matcher.find())
					builder.append(matcher.group());
			}
				
		}
		return builder.toString().replaceAll(" ", "");
	}

	public Autore getAutore()
	{
		return autore;
	}
	public void setAutore(Autore autore)
	{
		this.autore = autore;
	}
	public String getTesto()
	{
		return testo;
	}
	public void setTesto(String testo)
	{
		this.testo = testo;
	}

	@Override
	public String toString()
	{
		return this.getTesto() + " di " + this.getAutore().getNome();
	}
	
	
}
