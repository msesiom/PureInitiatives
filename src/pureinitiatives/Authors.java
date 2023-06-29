/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pureinitiatives;

/**
 *
 * @author morenom1
 */
import java.util.ArrayList;

public class Authors {
    
    private ArrayList<Author> autores;
    private String apiKey, apiVersion, baseURL;
       
    public Authors(String baseURL, String apiVersion, String apiKey)
    {
        this.autores = new ArrayList<>();
        this.baseURL = baseURL;
        this.apiVersion = apiVersion;
        this.apiKey = apiKey;
    }
    
    public ArrayList<Author> getArray()
    {
        return this.autores;
    }
    
    public void add(Author autor)
    {
        this.autores.add(autor);
    }
    
    public void setAuthor(int index, Author autor)
    {
        this.autores.set(index, autor);
    }
    
    public void setConceptNames(boolean newFPE)
    {
        for(Author au : autores)
        {
            au.setConceptNames(10, baseURL, apiVersion, apiKey, newFPE);
        }
    }
    
    public int getHigherNConcepts()
    {
        int tempN = 0;
        for(Author au : autores)
        {
            if(au.getNConcepts()>tempN)
                tempN = au.getNConcepts();
        }
        return tempN;
    }
    
    public int getNConcepts()
    {
        int tempN = 0;
        for(Author au : autores)
        {
            tempN = tempN + au.getNConcepts();
        }
        return tempN;
    }
    
    public int size()
    {
        return this.autores.size();
    }
    
}
