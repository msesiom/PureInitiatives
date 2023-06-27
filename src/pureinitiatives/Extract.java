/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pureinitiatives;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 *
 * @author morenom1
 */
public class Extract {
    
    private final int timeout = 15;
    private RequestConfig config;
    private String apiKey, apiVersion, baseURL, newApiKey;
    private String keywordsP1, keywordsP2, keywordsP3, keywordsP4, keywordsP5, keywordsP6, keywordsP7;
    private String startYear, endYear;
    private Authors autores;
    private boolean newFPE;
    private String jsessionID;
    
    public Extract(String baseURL, String apiVersion, String apiKey, boolean newFPE, String newApiKey)
    {
        this.baseURL = baseURL;
        this.apiVersion = apiVersion;
        this.apiKey = apiKey;
        this.newApiKey = newApiKey;
        this.autores = new Authors(baseURL, apiVersion, apiKey);
        this.newFPE = newFPE;
        config = RequestConfig.custom()
          .setConnectTimeout(timeout * 1000)
          .setConnectionRequestTimeout(timeout * 1000)
          .setSocketTimeout(timeout * 1000).build();
        this.keywordsP1 = "";
        this.keywordsP2 = "";
        this.keywordsP3 = "";
        this.keywordsP4 = "";
        this.keywordsP5 = "";
        this.keywordsP6 = "";
        this.keywordsP7 = "";
        this.jsessionID = "";
        this.startYear = "2016";
        this.endYear = "2020";
    }
    
    public void setPillarsKeywords(String keywordsP1, String keywordsP2, String keywordsP3, String keywordsP4, String keywordsP5, String keywordsP6, String keywordsP7)
    {
        this.keywordsP1 = keywordsP1;
        this.keywordsP2 = keywordsP2;
        this.keywordsP3 = keywordsP3;
        this.keywordsP4 = keywordsP4;
        this.keywordsP5 = keywordsP5;
        this.keywordsP6 = keywordsP6;
        this.keywordsP7 = keywordsP7;
    }
    
    public void setYears(String startYear, String endYear)
    {
        this.startYear = startYear;
        this.endYear = endYear;
    }
    
    public void getProfiles()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(baseURL);
        sb.append("/ws/api/");
        sb.append(apiVersion);
        sb.append("/persons?apiKey=");
        sb.append(apiKey);
        HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        String nextURL = sb.toString();
        try 
        {   
            System.out.println("Extracting persons from Pure: ");
            System.out.println(baseURL);      
            do
            {
                HttpGet request = new HttpGet(nextURL);
                request.addHeader("Accept", "application/json");
                request.addHeader("api-key", apiKey);
                request.addHeader("JSESSIONID", jsessionID);
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode()==200)
                {
                    jsessionID = getSessionID(response);
                    String result = EntityUtils.toString(response.getEntity());
                    Object obj = new JSONParser().parse(result);
                    JSONObject jsonObj = (JSONObject)obj;
                    JSONArray jsonSiguientes = (JSONArray) jsonObj.get("navigationLinks");
                    Iterator itrLinks = jsonSiguientes.iterator();
                    while(itrLinks.hasNext())
                    {
                        Map link = (Map) itrLinks.next();
                        String tipo = link.get("ref").toString();
                        if(tipo.equalsIgnoreCase("next"))
                        {
                            nextURL = link.get("href").toString();
                        }
                        else
                        {
                            nextURL = "";
                        }
                    }
                    JSONArray ja = (JSONArray)jsonObj.get("items");
    //                long contPersonas = (long)jsonObj.get("count");
                    Iterator itr = ja.iterator();
                    while(itr.hasNext())
                    {
                        Author autor = new Author();
                        Map entry = (Map) itr.next();
                        JSONObject name = (JSONObject)entry.get("name");
                        if(name.get("firstName")!=null)
                            autor.setNombre(name.get("firstName").toString());
                        autor.setApellido(name.get("lastName").toString());
                        autor.setPureId(entry.get("pureId").toString());
                        autor.setUUID(entry.get("uuid").toString());
                        JSONObject info = (JSONObject)entry.get("info");
                        JSONArray prettyIds = (JSONArray)info.get("prettyURLIdentifiers");
                        if(prettyIds!=null)
                            autor.setPrettyURL(prettyIds.get(0).toString());
                        //Visibility for not requesting and not updating those profiles
                        JSONObject visibility = (JSONObject)entry.get("visibility");
                        autor.setVisibility(visibility.get("key").toString());
                        System.out.println("Extracted: " + autor.getPureId() + " with prettyURL " + autor.getPrettyURL());
                        autores.add(autor);
                    }
                }
                
            }
            while(!nextURL.equalsIgnoreCase(""));        
        } catch (IOException ex) 
        {
            Logger.getLogger(Extract.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) 
        {
            Logger.getLogger(Extract.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    //Use the New API to obtain the profileInformation JSON section, because in the PUT it replaces the whoole JSONArray
    public void getProfilesInfo()
    {
        StringBuilder sb = new StringBuilder();
        HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        System.out.println("Extracting persons profile information from Pure: ");    
        for(Author au : autores.getArray())
        {
            try 
            {  
                sb = new StringBuilder();
                sb.append(baseURL);
                sb.append("/ws/api/persons/");
                sb.append(au.getUUID());
                HttpGet request = new HttpGet(sb.toString());
                request.addHeader("Accept", "application/json");
                request.addHeader("Content-Type", "application/json");
                request.addHeader("api-key", newApiKey);
                request.addHeader("JSESSIONID", jsessionID);
                System.out.println("Extracting profile information of: " + au.getPureId()); 
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode()==200)
                {
                    jsessionID = getSessionID(response);
                    String result = EntityUtils.toString(response.getEntity());
                    Object obj = new JSONParser().parse(result);
                    JSONObject jsonObj = (JSONObject)obj;
                    if(jsonObj.containsKey("profileInformation"))
                    {
                        JSONArray ja = (JSONArray)jsonObj.get("profileInformation");
                        Iterator itr = ja.iterator();
                        StringBuilder sbJSON = new StringBuilder();
                        int contInfos = 0;
                        while(itr.hasNext())
                        {

                            JSONObject entry = (JSONObject) itr.next();
                            JSONObject type = (JSONObject)entry.get("type");
                            if(!type.get("uri").toString().equalsIgnoreCase("/dk/atira/pure/person/customfields/research_strategic_pillar"))
                            {
                                //Save info
                                if(contInfos!=0)
                                    sbJSON.append(",");
                                sbJSON.append(entry.toJSONString());
                                contInfos++;
                            }                 
                        }
                        au.setProfileInfo(sbJSON.toString());
                    }
                }
            } catch (IOException ex) 
            {
                Logger.getLogger(Extract.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) 
            {
                Logger.getLogger(Extract.class.getName()).log(Level.SEVERE, null, ex);
            }
        }      
    }
    
    public void getFingerprints()
    {
        try 
        {   
            StringBuilder sb = new StringBuilder();
            HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
            String nextURL = sb.toString();
            //Todos los Autores, ahora ir por el Fingerprint
            System.out.println("Extracting Fingerprints...");
            for(Author au : autores.getArray())
            {
                sb = new StringBuilder();
                sb.append(baseURL);
                sb.append("/ws/api/");
                sb.append(apiVersion);
                sb.append("/persons/");
                sb.append(au.getPureId());
                System.out.println("Extracting person: " + au.getPureId());
                sb.append("/fingerprints");
                HttpGet request = new HttpGet(sb.toString());
                request.addHeader("Accept", "application/json");
                request.addHeader("api-key", apiKey);
                request.addHeader("JSESSIONID", jsessionID);
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode()==200)
                {
                    jsessionID = getSessionID(response);
                    String result = EntityUtils.toString(response.getEntity());
                    Object obj = new JSONParser().parse(result);
                    JSONObject jsonObj = (JSONObject)obj;
                    JSONArray ja = (JSONArray)jsonObj.get("items");
                    Iterator itr = ja.iterator();
                    while(itr.hasNext())
                    {
                        Map entry = (Map) itr.next();
                        JSONArray jsonConcepts = (JSONArray)entry.get("concepts");
                        Iterator itrConcepts = jsonConcepts.iterator();
                        while(itrConcepts.hasNext())
                        {
                            JSONObject concept = (JSONObject) itrConcepts.next();
                            Concept concepto = new Concept(concept.get("uuid").toString());
                            concepto.setRank(Double.valueOf(concept.get("rank").toString()));
                            concepto.setWRank(Double.valueOf(concept.get("weightedRank").toString()));
                            au.addConcept(concepto);
                        }
                    }
                    
                }
            }
            System.out.println("Extracting Concepts and Thesauri details...");
            autores.setConceptNames(newFPE);
        } catch (IOException ex) 
        {
            Logger.getLogger(Extract.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) 
        {
            Logger.getLogger(Extract.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getNumberRO()
    {
        try 
        {   
            StringBuilder sb = new StringBuilder();
            HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
            String nextURL = sb.toString();
            //Todos los Autores, ahora ir por las Publicaciones
            System.out.println("Extracting Number of RO...");
            for(Author au : autores.getArray())
            {
                sb = new StringBuilder();
                sb.append(baseURL);
                sb.append("/ws/api/");
                sb.append(apiVersion);
                sb.append("/persons/");
                sb.append(au.getPureId());
                System.out.println("Extracting count of RO for person: " + au.getPureId());
                sb.append("/research-outputs");
                HttpGet request = new HttpGet(sb.toString());
                request.addHeader("Accept", "application/json");
                request.addHeader("api-key", apiKey);
                request.addHeader("JSESSIONID", jsessionID);
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode()==200)
                {
                    jsessionID = getSessionID(response);
                    String result = EntityUtils.toString(response.getEntity());
                    Object obj = new JSONParser().parse(result);
                    JSONObject jsonObj = (JSONObject)obj;
                    au.setContRO(Integer.valueOf(jsonObj.get("count").toString()));
                }
            }
            
        } catch (IOException ex) 
        {
            Logger.getLogger(Extract.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) 
        {
            Logger.getLogger(Extract.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getAllPillars()
    {
        getNumberROinPillar(keywordsP1, 1);
        getNumberROinPillar(keywordsP2, 2);
        getNumberROinPillar(keywordsP3, 3);
        getNumberROinPillar(keywordsP4, 4);
        getNumberROinPillar(keywordsP5, 5);
        getNumberROinPillar(keywordsP6, 6);
//        getNumberROinPillar(keywordsP7, 7);
    }
    
    public void getNumberROinPillar(String keywordsP, int pillarNum)
    {
        StringBuilder sb = new StringBuilder();
        StringBuilder jsonBody = new StringBuilder();
        HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        //Todos los Autores, ahora ir por las Publicaciones
        System.out.println("Extracting Number of RO for pillar: " + pillarNum);
        for(Author au : autores.getArray())
        {
            if(au.getVisibility().equalsIgnoreCase("FREE"))
            {
                //Extract only for public profiles
                System.out.println("Extracting count of RO for person: " + au.getPureId());
                String keywords[] = keywordsP.split(",");
                for(String keyword : keywords)
                {
                    try 
                    { 
                        sb = new StringBuilder();
                        jsonBody = new StringBuilder();
                        sb.append(baseURL);
                        sb.append("/ws/api/");
                        sb.append(apiVersion);
    //                    System.out.println("Extracting count of RO for person: " + au.getPureId());
                        sb.append("/research-outputs");
                        HttpPost request = new HttpPost(sb.toString());
                        request.addHeader("Accept", "application/json");
                        request.addHeader("Content-Type", "application/json");
                        request.addHeader("api-key", apiKey);
                        request.addHeader("JSESSIONID", jsessionID);
                        jsonBody.append("{ \"publishedBeforeDate\": \"");
                        jsonBody.append(endYear);
    //                    jsonBody.append("2020");
                        jsonBody.append("-12-31\",");
                        jsonBody.append(" \"publishedAfterDate\": \"");
                        jsonBody.append(startYear);
    //                    jsonBody.append("2016");
                        jsonBody.append("-01-01\",");
                        jsonBody.append("\"forPersons\": {\"ids\": [\"");
                        jsonBody.append(au.getPureId());
                        jsonBody.append("\"]},");
                        jsonBody.append("\"searchString\": \"");
                        jsonBody.append(keyword.trim());
                        jsonBody.append("\"}");
                        StringEntity entity = new StringEntity(jsonBody.toString(),ContentType.APPLICATION_FORM_URLENCODED);
                        request.setEntity(entity);
                        HttpResponse response = client.execute(request);
                        if (response.getStatusLine().getStatusCode()==200)
                        {
                            jsessionID = getSessionID(response);
                            String result = EntityUtils.toString(response.getEntity());
                            Object obj = new JSONParser().parse(result);
                            JSONObject jsonObj = (JSONObject)obj;
                            au.addRO(pillarNum, Integer.valueOf(jsonObj.get("count").toString()));
                        }
                    } catch (IOException ex) 
                    {
                        Logger.getLogger(Extract.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) 
                    {
                        Logger.getLogger(Extract.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    
    public Authors getAuthors()
    {
        return this.autores;
    }
    
    //Method to extract authors and their publications    
    public void extractAuthorsPublications()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(baseURL);
        sb.append("/ws/api/");
        sb.append(apiVersion);
        sb.append("/persons");
        HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        String nextURL = sb.toString();
        try 
        {       
            System.out.println("Extracting persons from Pure: ");
            do
            {
                HttpGet request = new HttpGet(nextURL);
                request.addHeader("Accept", "application/json");
                request.addHeader("api-key", apiKey);
                request.addHeader("JSESSIONID", jsessionID);
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode()==200)
                {
                    jsessionID = getSessionID(response);
                    String result = EntityUtils.toString(response.getEntity());
                    Object obj = new JSONParser().parse(result);
                    JSONObject jsonObj = (JSONObject)obj;
                    JSONArray jsonSiguientes = (JSONArray) jsonObj.get("navigationLinks");
                    Iterator itrLinks = jsonSiguientes.iterator();
                    while(itrLinks.hasNext())
                    {
                        Map link = (Map) itrLinks.next();
                        String tipo = link.get("ref").toString();
                        if(tipo.equalsIgnoreCase("next"))
                        {
                            nextURL = link.get("href").toString();
                        }
                        else
                        {
                            nextURL = "";
                        }
                    }
                    JSONArray ja = (JSONArray)jsonObj.get("items");
    //                long contPersonas = (long)jsonObj.get("count");
                    Iterator itr = ja.iterator();
                    while(itr.hasNext())
                    {
                        Author autor = new Author();
                        Map entry = (Map) itr.next();
                        JSONObject name = (JSONObject)entry.get("name");
                        autor.setNombre(name.get("firstName").toString());
                        autor.setApellido(name.get("lastName").toString());
                        autor.setPureId(entry.get("pureId").toString());   
                        autor.setUUID(entry.get("uuid").toString());
                        autores.add(autor);
                    }
                }   
            }
            while(!nextURL.equalsIgnoreCase(""));
            //Todos los Autores, ahora ir por Publicaciones
            System.out.println("Extracting Research Output...");
            int contAu = 0;
            for(Author au : autores.getArray())
            {
                sb = new StringBuilder();
                sb.append(baseURL);
                sb.append("/ws/api/");
                sb.append(apiVersion);
                sb.append("/persons/");
                sb.append(au.getPureId());
                System.out.println("Extracting person: " + au.getPureId());
                sb.append("/research-outputs");
                nextURL = sb.toString();
                do
                {
                    HttpGet request = new HttpGet(nextURL);
                    request.addHeader("Accept", "application/json");
                    request.addHeader("api-key", apiKey);
                    request.addHeader("JSESSIONID", jsessionID);
                    HttpResponse response = client.execute(request);
                    if (response.getStatusLine().getStatusCode()==200)
                    {
                        jsessionID = getSessionID(response);
                        String result = EntityUtils.toString(response.getEntity());
                        Object obj = new JSONParser().parse(result);
                        JSONObject jsonObj = (JSONObject)obj;
                        JSONArray jsonSiguientes = (JSONArray) jsonObj.get("navigationLinks");
                        Iterator itrLinks = jsonSiguientes.iterator();
                        while(itrLinks.hasNext())
                        {
                            Map link = (Map) itrLinks.next();
                            String tipo = link.get("ref").toString();
                            if(tipo.equalsIgnoreCase("next"))
                            {
                                nextURL = link.get("href").toString();
                            }
                            else
                            {
                                nextURL = "";
                            }
                        }
                        JSONArray ja = (JSONArray)jsonObj.get("items");
                        Iterator itr = ja.iterator();
                        while(itr.hasNext())
                        {
                            Map entry = (Map) itr.next();
                            ResearchOutput pub = new ResearchOutput(entry.get("pureId").toString());
                            pub.setTitle(entry.get("title").toString());
                            if(entry.containsKey("abstracts"))
                            {
                                JSONArray jsonAbstracts = (JSONArray)entry.get("abstracts");
                                Iterator itrAbstracts = jsonAbstracts.iterator();
                                while(itrAbstracts.hasNext())
                                {
                                    JSONObject resumen = (JSONObject) itrAbstracts.next();
                                    pub.setAbstract(resumen.get("value").toString());
                                }
                            }
                            au.addResearch(pub);
                        }
                    }
                }
                while(!nextURL.equalsIgnoreCase(""));
                autores.setAuthor(contAu, au);
                contAu++;
            }
            System.out.println("Adding profile details...");
            //Aquí pongo la información de su perfil
            for(Author au : autores.getArray())
            {
                sb = new StringBuilder();
                sb.append(baseURL);
                sb.append("/ws/api/");
                sb.append(apiVersion);
                sb.append("/persons/");
                sb.append(au.getPureId());
                HttpGet request = new HttpGet(sb.toString());
                request.addHeader("Accept", "application/json");
                request.addHeader("api-key", apiKey);
                request.addHeader("JSESSIONID", jsessionID);
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode()==200)
                {
                    jsessionID = getSessionID(response);
                    String result = EntityUtils.toString(response.getEntity());
                    Object obj = new JSONParser().parse(result);
                    JSONObject jsonObj = (JSONObject)obj;
                    JSONArray jsonInformacion = (JSONArray) jsonObj.get("profileInformations");
                    Iterator itrInfo = jsonInformacion.iterator();
                    while(itrInfo.hasNext())
                    {
                        Map link = (Map) itrInfo.next();
                        String tipo = link.get("ref").toString();
                    }
                }
            }
            
            System.out.println("Extracting Concepts and Thesauri details...");
            
        } catch (IOException ex) 
        {
            Logger.getLogger(Extract.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) 
        {
            Logger.getLogger(Extract.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getSessionID(HttpResponse response)
    {
        if(jsessionID.equalsIgnoreCase(""))
        {
            String tempCookieS = "";
            Header[] cookies = response.getAllHeaders();
            for(Header cookie : cookies)
            {
                if(cookie.getName().equalsIgnoreCase("Set-Cookie"))
                {
                    tempCookieS = cookie.getValue();
                    //Format JSESSIONID=9C1807BB2C88CDD3EEC22FC74A2BF89D; Path=/ws; Secure; HttpOnly; SameSite=Lax
                    tempCookieS = tempCookieS.substring(tempCookieS.indexOf("JSESSIONID=")+11, tempCookieS.indexOf(";"));
                    return tempCookieS;
                }

            }
            return "";
        }
        else
            return jsessionID;
    }
    
}
