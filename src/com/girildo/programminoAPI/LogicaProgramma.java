package com.girildo.programminoAPI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.flickr4java.flickr.FlickrException;
import com.girildo.programminoAPI.Messaggio.FlagMessaggio;

public abstract class LogicaProgramma 
{
	protected final static String KEY = "00b9cc2a3bf5e2896905d1fd621a20eb";
	protected final static String SECRET = "a5be5f21bd03edc2";
	
	public LogicaProgramma()
	{
		Commento.resetVotingFlag();
	}
	
	public Messaggio caricaDaLink(String url)
	{
		Pattern pattern = Pattern.compile(".+\\/discuss\\/");
		Matcher matcher = pattern.matcher(url);
		String idSporco = matcher.replaceAll("");
		String id = idSporco.endsWith("/")?idSporco.substring(0, idSporco.length()-1):idSporco;
		try
		{
			return new Messaggio(ottieniCommenti(id), FlagMessaggio.NESSUN_ERRORE);
		}
		catch(FlickrException ex)
		{
			if(ex.getErrorCode().equalsIgnoreCase("1"))
				return new Messaggio("Non ho trovato un topic di Flickr valido a quel link."
						+ "\nAssicurati di aver copiato l'URL per intero", FlagMessaggio.ERRORE);
			return new Messaggio(null, FlagMessaggio.ERRORE);
		}
		catch(IllegalArgumentException ex)
		{
			return new Messaggio(ex.getMessage(), FlagMessaggio.ERRORE);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	protected abstract String ottieniCommenti(String id) throws FlickrException;
	public abstract Messaggio GeneraClassifica(int numPreferenze);
	
}
