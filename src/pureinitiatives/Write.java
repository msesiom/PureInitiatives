/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pureinitiatives;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author morenom1
 */
public class Write {
    
    private final int timeout = 15;
    private RequestConfig config;
    private String apiKey, apiVersion, baseURL, language;
    private String jsessionID;
    private int threshold;
    private Authors autores;
    private String p1Link, p1Image, p2Link, p2Image, p3Link, p3Image, p4Link, p4Image, p5Link, p5Image, p6Link, p6Image, p7Link, p7Image;
    private String p1Name, p1URI, p2Name, p2URI, p3Name, p3URI, p4Name, p4URI, p5Name, p5URI, p6Name, p6URI, p7Name, p7URI;
   
    public Write(String baseURL, String apiVersion, String apiKey, Authors autores, int threshold, String language)
    {
        this.baseURL = baseURL;
        this.apiVersion = apiVersion;
        this.apiKey = apiKey;
        config = RequestConfig.custom()
          .setConnectTimeout(timeout * 1000)
          .setConnectionRequestTimeout(timeout * 1000)
          .setSocketTimeout(timeout * 1000).build();
        this.jsessionID = "";
        this.threshold = threshold;
        this.autores = autores;
        this.language = language;
    }
    
    public void setupPillars(String p1Link, String p1Image, String p2Link, String p2Image, String p3Link, String p3Image, String p4Link, String p4Image, String p5Link, String p5Image, String p6Link, String p6Image, String p7Link, String p7Image)
    {
        this.p1Link = p1Link;
        this.p1Image = p1Image;
        this.p2Link = p2Link;
        this.p2Image = p2Image;
        this.p3Link = p3Link;
        this.p3Image = p3Image;
        this.p4Link = p4Link;
        this.p4Image = p4Image;
        this.p5Link = p5Link;
        this.p5Image = p5Image;
        this.p6Link = p6Link;
        this.p6Image = p6Image;
        this.p7Link = p7Link;
        this.p7Image = p7Image;
    }
    
    public void setupClassPillars(String p1Name, String p1URI, String p2Name, String p2URI, String p3Name, String p3URI, String p4Name, String p4URI, String p5Name, String p5URI, String p6Name, String p6URI, String p7Name, String p7URI)
    {
        this.p1Name = p1Name;
        this.p1URI = p1URI;
        this.p2Name = p2Name;
        this.p2URI = p2URI;
        this.p3Name = p3Name;
        this.p3URI = p3URI;
        this.p4Name = p4Name;
        this.p4URI = p4URI;
        this.p5Name = p5Name;
        this.p5URI = p5URI;
        this.p6Name = p6Name;
        this.p6URI = p6URI;
        this.p7Name = p7Name;
        this.p7URI = p7URI;
    }
    
    public void writePillars()
    {
        try 
        {   
            StringBuilder sb = new StringBuilder();
            HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
            //Todos los Autores, ahora ir por las Publicaciones
            System.out.println("Setting Pillars");
            for(Author au : autores.getArray())
            {
                //Check if we need to update pillars
                if(au.checkNPillars(threshold)>0)
                {
                    sb = new StringBuilder();
                    sb.append(baseURL);
                    sb.append("/ws/api/persons/");
                    sb.append(au.getUUID());
                    System.out.println("Setting Pillars for person: " + au.getPureId());
                    HttpPut request = new HttpPut(sb.toString());
                    request.addHeader("Accept", "application/json");
                    request.addHeader("Content-Type", "application/json");
                    request.addHeader("api-key", apiKey);
                    request.addHeader("JSESSIONID", jsessionID);
                    StringEntity entity = new StringEntity(getPillarJSONValue(au),ContentType.APPLICATION_FORM_URLENCODED);
                    request.setEntity(entity);
                    HttpResponse response = client.execute(request);
                    if (response.getStatusLine().getStatusCode()==200)
                    {
                        jsessionID = getSessionID(response);
                        String result = EntityUtils.toString(response.getEntity());
                        Object obj = new JSONParser().parse(result);
                        JSONObject jsonObj = (JSONObject)obj;
                        System.out.println("Pillars updated Successfully");
                    }
                    else
                    {
                        System.out.println(response.getStatusLine().getReasonPhrase());
                        System.out.println(getPillarJSONValue(au));
                    }
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
    
    public String getPillarJSONValue(Author au)
    {
        int tempContP = 0;
        
        StringBuilder sbPillarsJSONvalue = new StringBuilder();
        StringBuilder sbClassPillarsJSONvalue = new StringBuilder();
        sbPillarsJSONvalue.append("{\"profileInformation\": [{\"value\": {");
        sbPillarsJSONvalue.append("\"");
        //Language
        sbPillarsJSONvalue.append(language);
        sbPillarsJSONvalue.append("\": \"<p style=\\\"text-align: justify;\\\">Dr. Randall Urban, VP and Chief Research Officer, has determined that UTMB research should be prioritized into <a href=\\\"https://research.utmb.edu/research-at-utmb/strategic-plan\\\">six health communities</a>. This researcher has received the following badge(s):</p>\\n");
        if(au.getContPillar1()>=threshold)
        {
            sbPillarsJSONvalue.append("<a href=\\\"");
            sbPillarsJSONvalue.append(p1Link);
            sbPillarsJSONvalue.append("\\\">");
            sbPillarsJSONvalue.append("<img src=\\\"");
            sbPillarsJSONvalue.append(p1Image);
            sbPillarsJSONvalue.append("\" alt=\\\"\\\" height=\\\"100\\\" width=\\\"100\\\"/>");
            sbPillarsJSONvalue.append("</a>");
            tempContP++;
            //Classification in Keywords
            sbClassPillarsJSONvalue.append("{\"uri\": \"");
            //URI
            sbClassPillarsJSONvalue.append(p1URI);
            sbClassPillarsJSONvalue.append("\", \"term\": {\"");
            sbClassPillarsJSONvalue.append(language);
            sbClassPillarsJSONvalue.append("\": \"");
            //Name
            sbClassPillarsJSONvalue.append(p1Name);
            sbClassPillarsJSONvalue.append("\"}}");
        }
        if(au.getContPillar2()>=threshold)
        {
            if(tempContP>0)
            {
                sbPillarsJSONvalue.append(" ");
                sbClassPillarsJSONvalue.append(",");
            }
            sbPillarsJSONvalue.append("<a href=\\\"");
            sbPillarsJSONvalue.append(p2Link);
            sbPillarsJSONvalue.append("\\\">");
            sbPillarsJSONvalue.append("<img src=\\\"");
            sbPillarsJSONvalue.append(p2Image);
            sbPillarsJSONvalue.append("\" alt=\\\"\\\" height=\\\"100\\\" width=\\\"100\\\"/>");
            sbPillarsJSONvalue.append("</a>");
            tempContP++;
            //Classification in Keywords
            sbClassPillarsJSONvalue.append("{\"uri\": \"");
            //URI
            sbClassPillarsJSONvalue.append(p2URI);
            sbClassPillarsJSONvalue.append("\", \"term\": {\"");
            sbClassPillarsJSONvalue.append(language);
            sbClassPillarsJSONvalue.append("\": \"");
            //Name
            sbClassPillarsJSONvalue.append(p2Name);
            sbClassPillarsJSONvalue.append("\"}}");
        }
        if(au.getContPillar3()>=threshold)
        {
            if(tempContP>0)
            {
                sbPillarsJSONvalue.append(" ");
                sbClassPillarsJSONvalue.append(",");
            }
            sbPillarsJSONvalue.append("<a href=\\\"");
            sbPillarsJSONvalue.append(p3Link);
            sbPillarsJSONvalue.append("\\\">");
            sbPillarsJSONvalue.append("<img src=\\\"");
            sbPillarsJSONvalue.append(p3Image);
            sbPillarsJSONvalue.append("\" alt=\\\"\\\" height=\\\"100\\\" width=\\\"100\\\"/>");
            sbPillarsJSONvalue.append("</a>");
            tempContP++;
            //Classification in Keywords
            sbClassPillarsJSONvalue.append("{\"uri\": \"");
            //URI
            sbClassPillarsJSONvalue.append(p3URI);
            sbClassPillarsJSONvalue.append("\", \"term\": {\"");
            sbClassPillarsJSONvalue.append(language);
            sbClassPillarsJSONvalue.append("\": \"");
            //Name
            sbClassPillarsJSONvalue.append(p3Name);
            sbClassPillarsJSONvalue.append("\"}}");
        }
        if(au.getContPillar4()>=threshold)
        {
            if(tempContP>0)
            {
                sbPillarsJSONvalue.append(" ");
                sbClassPillarsJSONvalue.append(",");
            }
            sbPillarsJSONvalue.append("<a href=\\\"");
            sbPillarsJSONvalue.append(p4Link);
            sbPillarsJSONvalue.append("\\\">");
            sbPillarsJSONvalue.append("<img src=\\\"");
            sbPillarsJSONvalue.append(p4Image);
            sbPillarsJSONvalue.append("\" alt=\\\"\\\" height=\\\"100\\\" width=\\\"100\\\"/>");
            sbPillarsJSONvalue.append("</a>");
            tempContP++;
            //Classification in Keywords
            sbClassPillarsJSONvalue.append("{\"uri\": \"");
            //URI
            sbClassPillarsJSONvalue.append(p4URI);
            sbClassPillarsJSONvalue.append("\", \"term\": {\"");
            sbClassPillarsJSONvalue.append(language);
            sbClassPillarsJSONvalue.append("\": \"");
            //Name
            sbClassPillarsJSONvalue.append(p4Name);
            sbClassPillarsJSONvalue.append("\"}}");
        }
        if(au.getContPillar5()>=threshold)
        {
            if(tempContP>0)
            {
                sbPillarsJSONvalue.append(" ");
                sbClassPillarsJSONvalue.append(",");
            }
            sbPillarsJSONvalue.append("<a href=\\\"");
            sbPillarsJSONvalue.append(p5Link);
            sbPillarsJSONvalue.append("\\\">");
            sbPillarsJSONvalue.append("<img src=\\\"");
            sbPillarsJSONvalue.append(p5Image);
            sbPillarsJSONvalue.append("\" alt=\\\"\\\" height=\\\"100\\\" width=\\\"100\\\"/>");
            sbPillarsJSONvalue.append("</a>");
            tempContP++;
            //Classification in Keywords
            sbClassPillarsJSONvalue.append("{\"uri\": \"");
            //URI
            sbClassPillarsJSONvalue.append(p5URI);
            sbClassPillarsJSONvalue.append("\", \"term\": {\"");
            sbClassPillarsJSONvalue.append(language);
            sbClassPillarsJSONvalue.append("\": \"");
            //Name
            sbClassPillarsJSONvalue.append(p5Name);
            sbClassPillarsJSONvalue.append("\"}}");
        }
        if(au.getContPillar6()>=threshold)
        {
            if(tempContP>0)
            {
                sbPillarsJSONvalue.append(" ");
                sbClassPillarsJSONvalue.append(",");
            }
            sbPillarsJSONvalue.append("<a href=\\\"");
            sbPillarsJSONvalue.append(p6Link);
            sbPillarsJSONvalue.append("\\\">");
            sbPillarsJSONvalue.append("<img src=\\\"");
            sbPillarsJSONvalue.append(p6Image);
            sbPillarsJSONvalue.append("\" alt=\\\"\\\" height=\\\"100\\\" width=\\\"100\\\"/>");
            sbPillarsJSONvalue.append("</a>");
            tempContP++;
            //Classification in Keywords
            sbClassPillarsJSONvalue.append("{\"uri\": \"");
            //URI
            sbClassPillarsJSONvalue.append(p6URI);
            sbClassPillarsJSONvalue.append("\", \"term\": {\"");
            sbClassPillarsJSONvalue.append(language);
            sbClassPillarsJSONvalue.append("\": \"");
            //Name
            sbClassPillarsJSONvalue.append(p6Name);
            sbClassPillarsJSONvalue.append("\"}}");
        }
        if(au.getContPillar7()>=threshold)
        {
            if(tempContP>0)
            {
                sbPillarsJSONvalue.append(" ");
                sbClassPillarsJSONvalue.append(",");
            }
            sbPillarsJSONvalue.append("<a href=\\\"");
            sbPillarsJSONvalue.append(p7Link);
            sbPillarsJSONvalue.append("\\\">");
            sbPillarsJSONvalue.append("<img src=\\\"");
            sbPillarsJSONvalue.append(p7Image);
            sbPillarsJSONvalue.append("\" alt=\\\"\\\" height=\\\"100\\\" width=\\\"100\\\"/>");
            sbPillarsJSONvalue.append("</a>");
            tempContP++;
            //Classification in Keywords
            sbClassPillarsJSONvalue.append("{\"uri\": \"");
            //URI
            sbClassPillarsJSONvalue.append(p7URI);
            sbClassPillarsJSONvalue.append("\", \"term\": {\"");
            sbClassPillarsJSONvalue.append(language);
            sbClassPillarsJSONvalue.append("\": \"");
            //Name
            sbClassPillarsJSONvalue.append(p7Name);
            sbClassPillarsJSONvalue.append("\"}}");
        }
        sbPillarsJSONvalue.append("\"");
        sbPillarsJSONvalue.append("},\"type\":{\"uri\":\"");
        //URI
        sbPillarsJSONvalue.append("/dk/atira/pure/person/customfields/research_strategic_pillar");
        //Term
        sbPillarsJSONvalue.append("\",\"term\": {\"");
        //Language
        sbPillarsJSONvalue.append(language);
        sbPillarsJSONvalue.append("\":\"");
        sbPillarsJSONvalue.append("Research Strategic Pillar");
        sbPillarsJSONvalue.append("\" }}}");
        
        //If existing 
        if(!au.getProfileInfo().isEmpty())
        {
            sbPillarsJSONvalue.append(",");
            sbPillarsJSONvalue.append(au.getProfileInfo());
        }
        sbPillarsJSONvalue.append("]");
        
        
        //Keyword config for Pure filters
        sbPillarsJSONvalue.append(",\"keywordGroups\": [{\"logicalName\": \"");
        sbPillarsJSONvalue.append("/dk/atira/pure/rsp_keywords");
        sbPillarsJSONvalue.append("\",\"name\": {\"");
        sbPillarsJSONvalue.append(language);
        sbPillarsJSONvalue.append("\": \"");
        sbPillarsJSONvalue.append("Research Strategic Pillar Keywords");
        sbPillarsJSONvalue.append("\"}, \"classifications\": [");
        sbPillarsJSONvalue.append(sbClassPillarsJSONvalue.toString());
        sbPillarsJSONvalue.append("], \"typeDiscriminator\": \"ClassificationsKeywordGroup\"}]}");
        return sbPillarsJSONvalue.toString();
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
