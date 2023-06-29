/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pureinitiatives;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author morenom1
 */
public class Author {
    
    private String uuid, pureId, scopusId, orcid, nombre, apellido, orgPureId, orgNombre, orgScopusId, orgPureId2, orgNombre2, orgScopusId2;
    private String profile, researchI, prettyURL, profileInfo, visibility;
    //This is for an author with more than one scopus profile. It points to main Scopus Author ID
    private String scopusId0, rawScopusId;
    private ArrayList<Concept> fingerprint;
    private ArrayList<Concept> fPResearchInterests;
    private ArrayList<Concept> fPProfile;
    private ArrayList<ResearchOutput> research;
    private int contRO, contPillar1, contPillar2, contPillar3, contPillar4, contPillar5, contPillar6, contPillar7, nPillars;
    private boolean boolP1, boolP2, boolP3, boolP4, boolP5, boolP6, boolP7;

    public Author()
    {
        this.uuid = "";
        this.pureId = "";
        this.nombre = "";
        this.apellido = "";
        this.orcid = "";
        this.scopusId = "";
        this.orgPureId = "";
        this.orgNombre = "";
        this.orgScopusId = "";
        this.scopusId0 = "";
        this.rawScopusId = "";
        this.profile = "";
        this.researchI = "";
        this.prettyURL = "";
        this.profileInfo = "";
        this.fingerprint = new ArrayList<>();
        this.fPProfile = new ArrayList<>();
        this.fPResearchInterests = new ArrayList<>();
        this.research = new ArrayList<>();
        this.contRO = 0;
        this.contPillar1 = 0;
        this.contPillar2 = 0;
        this.contPillar3 = 0;
        this.contPillar4 = 0;
        this.contPillar5 = 0;
        this.contPillar6 = 0;
        this.contPillar7 = 0;
        this.nPillars = 0;
        this.boolP1 = false;
        this.boolP2 = false;
        this.boolP3 = false;
        this.boolP4 = false;
        this.boolP5 = false;
        this.boolP6 = false;
        this.boolP7 = false;
        this.visibility = "";
    }
    
    public Author(String pureId)
    {
        this.uuid = "";
        this.pureId = pureId;
        this.nombre = "";
        this.apellido = "";
        this.orcid = "";
        this.scopusId = "";
        this.orgPureId = "";
        this.orgNombre = "";
        this.orgScopusId = "";
        this.scopusId0 = "";
        this.rawScopusId = "";
        this.profile = "";
        this.researchI = "";
        this.prettyURL = "";
        this.profileInfo = "";
        this.fingerprint = new ArrayList<>();
        this.fPProfile = new ArrayList<>();
        this.fPResearchInterests = new ArrayList<>();
        this.research = new ArrayList<>();
        this.contRO = 0;
        this.contPillar1 = 0;
        this.contPillar2 = 0;
        this.contPillar3 = 0;
        this.contPillar4 = 0;
        this.contPillar5 = 0;
        this.contPillar6 = 0;
        this.contPillar7 = 0;
        this.nPillars = 0;
        this.boolP1 = false;
        this.boolP2 = false;
        this.boolP3 = false;
        this.boolP4 = false;
        this.boolP5 = false;
        this.boolP6 = false;
        this.boolP7 = false;
        this.visibility = "";
    }
    
    public Author(String nombre, String apellido, String pureId, String uuid, int contPillar1, int contPillar2, int contPillar3, int contPillar4, int contPillar5, int contPillar6, int contPillar7)
    {
        this.uuid = uuid;
        this.pureId = pureId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.orcid = "";
        this.scopusId = "";
        this.orgPureId = "";
        this.orgNombre = "";
        this.orgScopusId = "";
        this.scopusId0 = "";
        this.rawScopusId = "";
        this.profile = "";
        this.researchI = "";
        this.prettyURL = "";
        this.profileInfo = "";
        this.fingerprint = new ArrayList<>();
        this.fPProfile = new ArrayList<>();
        this.fPResearchInterests = new ArrayList<>();
        this.research = new ArrayList<>();
        this.contRO = 0;
        this.contPillar1 = contPillar1;
        this.contPillar2 = contPillar2;
        this.contPillar3 = contPillar3;
        this.contPillar4 = contPillar4;
        this.contPillar5 = contPillar5;
        this.contPillar6 = contPillar6;
        this.contPillar7 = contPillar7;
        this.nPillars = 0;
        this.boolP1 = false;
        this.boolP2 = false;
        this.boolP3 = false;
        this.boolP4 = false;
        this.boolP5 = false;
        this.boolP6 = false;
        this.boolP7 = false;
        this.visibility = "";
    }
    
    public boolean has2Org()
    {
        if (orgPureId2.isEmpty())
            return false;
        else
            return true;
    }

    public String getPureId()
    {
        return this.pureId;
    }

    public void setPureId(String pureId)
    {
        this.pureId = pureId;
    }
    
    public void setUUID(String uuid)
    {
        this.uuid = uuid;
    }
    
    public String getUUID()
    {
        return this.uuid;
    }
    
    public String getProfileInfo()
    {
        return this.profileInfo;
    }

    public void setProfileInfo(String profileInfo)
    {
        this.profileInfo = profileInfo;
    }
    
    public String getPrettyURL()
    {
        return this.prettyURL;
    }

    public void setPrettyURL(String prettyURL)
    {
        this.prettyURL = prettyURL;
    }

    public String getScopusId()
    {
        return this.scopusId;
    }

    public void setScopusId(String scopusId)
    {
        this.scopusId = scopusId;
    }

    public String getScopusId0()
    {
        return this.scopusId0;
    }

    public void setScopusId0(String scopusId0)
    {
        this.scopusId0 = scopusId0;
    }

    public String getRawScopusId()
    {
        return this.rawScopusId;
    }

    public void setRawScopusId(String rawScopusId)
    {
        this.rawScopusId = rawScopusId;
    }
    
    public String getVisibility()
    {
        return this.visibility;
    }

    public void setVisibility(String visibility)
    {
        this.visibility = visibility;
    }

    public String getNombre()
    {
        return this.nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getApellido()
    {
        return this.apellido;
    }

    public void setApellido(String apellido)
    {
        this.apellido = apellido;
    }

    public String getOrcid()
    {
        return this.orcid;
    }

    public void setOrcid(String orcid)
    {
        this.orcid = orcid;
    }

    public String getOrgPureId()
    {
        return this.orgPureId;
    }

    public void setOrgPureId(String orgPureId)
    {
        this.orgPureId = orgPureId;
    }

    public String getOrgNombre()
    {
        return this.orgNombre;
    }

    public void setOrgNombre(String orgNombre)
    {
        this.orgNombre = orgNombre;
    }

    public String getOrgScopusId()
    {
        return this.orgScopusId;
    }

    public void setOrgScopusId(String orgScopusId)
    {
        this.orgScopusId = orgScopusId;
    }

    public String getOrgPureId2()
    {
        return this.orgPureId2;
    }

    public void setOrgPureId2(String orgPureId2)
    {
        this.orgPureId2 = orgPureId2;
    }

    public String getOrgNombre2()
    {
        return this.orgNombre2;
    }

    public void setOrgNombre2(String orgNombre2)
    {
        this.orgNombre2 = orgNombre2;
    }

    public String getOrgScopusId2()
    {
        return this.orgScopusId2;
    }

    public void setOrgScopusId2(String orgScopusId2)
    {
        this.orgScopusId2 = orgScopusId2;
    }
    
    public String getProfile()
    {
        return this.profile;
    }

    public void setProfile(String profile)
    {
        this.profile = profile;
    }
    
    public String getResearchInterests()
    {
        return this.researchI;
    }

    public void setResearchInterests(String researchI)
    {
        this.researchI = researchI;
    }
    
    public void addConcept(Concept concepto)
    {
        this.fingerprint.add(concepto);
    }
    
    public void addConcept2(Concept concepto)
    {
        //Total aggregated Fingerprint
        int posC = posConcept(concepto.getUuid());
        if(posC==-1)
            this.fingerprint.add(concepto);
        else
        {
            Concept temp = this.fingerprint.get(posC);
            temp.setRank(temp.getRank()+concepto.getRank());
            temp.setFreq(temp.getFreq()+concepto.getFreq());
            this.fingerprint.set(posC, temp);
        }
        //Think for other alternatives: 5 Years, Only one thesauri
    }
    
    public void addConcept2FPProfile(Concept concepto)
    {
        //Total aggregated Fingerprint
        int posC = posConceptFPP(concepto.getUuid());
        if(posC==-1)
            this.fPProfile.add(concepto);
        else
        {
            Concept temp = this.fPProfile.get(posC);
            temp.setRank(temp.getRank()+concepto.getRank());
            temp.setFreq(temp.getFreq()+concepto.getFreq());
            this.fPProfile.set(posC, temp);
        }
    }
    
    public void addConcept2FPRI(Concept concepto)
    {
        //Total aggregated Fingerprint
        int posC = posConceptFPRI(concepto.getUuid());
        if(posC==-1)
            this.fPResearchInterests.add(concepto);
        else
        {
            Concept temp = this.fPResearchInterests.get(posC);
            temp.setRank(temp.getRank()+concepto.getRank());
            temp.setFreq(temp.getFreq()+concepto.getFreq());
            this.fPResearchInterests.set(posC, temp);
        }
    }
    
    public int getNConcepts()
    {
        return this.fingerprint.size();
    }
    
    public void setConceptNames(int timeout, String baseURL, String apiVersion, String apiKey, boolean newFPE)
    {
        RequestConfig config = RequestConfig.custom()
          .setConnectTimeout(timeout * 1000)
          .setConnectionRequestTimeout(timeout * 1000)
          .setSocketTimeout(timeout * 1000).build();
        HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        //El nombre no viene y hay que pedir cada concepto
        for(Concept concepto : this.fingerprint)
        {
            StringBuilder sb = new StringBuilder();
            sb.append(baseURL);
            sb.append("/ws/api/");
            sb.append(apiVersion);
            sb.append("/concepts/");
            sb.append(concepto.getUuid());
            System.out.println("Extracting Concept: " + concepto.getUuid());
            HttpGet request = new HttpGet(sb.toString());
            request.addHeader("Accept", "application/json");
            request.addHeader("api-key", apiKey);
            try 
            {  
                HttpResponse response = client.execute(request);
                if (response.getStatusLine().getStatusCode()==200)
                {
                    String result = EntityUtils.toString(response.getEntity());
                    Object obj = new JSONParser().parse(result);
                    JSONObject jsonObj = (JSONObject)obj;
                    if(newFPE)
                    {
                        //Omniscience concepts are not in the terms object
                        JSONObject conceptObj = (JSONObject)jsonObj.get("name");
                        JSONArray ja = (JSONArray)conceptObj.get("text");
                        JSONObject nameConcept = (JSONObject)ja.get(0);
                        concepto.setName(nameConcept.get("value").toString());
                    }
                    else
                    {
                        JSONArray ja = (JSONArray)jsonObj.get("terms");
                        Iterator itr = ja.iterator();
                        while(itr.hasNext())
                        {
                            JSONObject name = (JSONObject) itr.next();
                            concepto.setName(name.get("value").toString());
                        }
                    }
                    JSONObject thesauriObj = (JSONObject)jsonObj.get("thesauri");
                    JSONObject nameObj = (JSONObject)thesauriObj.get("name");
                    JSONArray jaThesauri = (JSONArray)nameObj.get("text");
                    Iterator itrThesauri = jaThesauri.iterator();
                    while(itrThesauri.hasNext())
                    {
                        JSONObject nameT = (JSONObject) itrThesauri.next();
                        concepto.setThesauri(nameT.get("value").toString());
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
    
    public ArrayList<Concept> getFingerprint()
    {
        return this.fingerprint;
    }
    
    public ArrayList<Concept> getFingerprintProfile()
    {
        return this.fPProfile;
    }
    
    public ArrayList<Concept> getFingerprintRI()
    {
        return this.fPResearchInterests;
    }
    
    public ArrayList<ResearchOutput> getResearch()
    {
        return this.research;
    }
    
    public void addResearch(ResearchOutput researchOutput)
    {
        this.research.add(researchOutput);
    }
    
    public int posConcept(String id)
    {
        boolean encontrado = false;
        int cont = 0;
        while ((!encontrado) && (cont != fingerprint.size()))
        {
            if (fingerprint.get(cont).getUuid().equals(id))
                encontrado = true;
            cont++;
        }
        cont--;
        if(encontrado)
            return cont;
        else
            return -1;
    }
    
    public int posConceptFPP(String id)
    {
        boolean encontrado = false;
        int cont = 0;
        while ((!encontrado) && (cont != fPProfile.size()))
        {
            if (fPProfile.get(cont).getUuid().equals(id))
                encontrado = true;
            cont++;
        }
        cont--;
        if(encontrado)
            return cont;
        else
            return -1;
    }
    
    public int posConceptFPRI(String id)
    {
        boolean encontrado = false;
        int cont = 0;
        while ((!encontrado) && (cont != fPResearchInterests.size()))
        {
            if (fPResearchInterests.get(cont).getUuid().equals(id))
                encontrado = true;
            cont++;
        }
        cont--;
        if(encontrado)
            return cont;
        else
            return -1;
    }
    
    
    
    public void setContRO(int contRO)
    {
        this.contRO = contRO;
    }
    
    public void addRO(int pillarNum, int contRO)
    {
        switch(pillarNum)
        {
            case 1: this.contPillar1 = this.contPillar1 + contRO;
                    this.boolP1 = true;
                break;
            
            case 2: this.contPillar2 = this.contPillar2 + contRO;
                    this.boolP2 = true;
                break;
            
            case 3: this.contPillar3 = this.contPillar3 + contRO;
                    this.boolP3 = true;
                break;
            
            case 4: this.contPillar4 = this.contPillar4 + contRO;
                    this.boolP4 = true;
                break;
            
            case 5: this.contPillar5 = this.contPillar5 + contRO;
                    this.boolP5 = true;
                break;
            
            case 6: this.contPillar6 = this.contPillar6 + contRO;
                    this.boolP6 = true;
                break;
            
            case 7: this.contPillar7 = this.contPillar7 + contRO;
                    this.boolP7 = true;
                break;
            
            default: 
                break;
        }
        
    }
    
    public int checkNPillars(int threshold)
    {
        nPillars = 0;
        if(contPillar1>=threshold)
            nPillars++;
        if(contPillar2>=threshold)
            nPillars++;
        if(contPillar3>=threshold)
            nPillars++;
        if(contPillar4>=threshold)
            nPillars++;
        if(contPillar5>=threshold)
            nPillars++;
        if(contPillar6>=threshold)
            nPillars++;
        if(contPillar7>=threshold)
            nPillars++;
        return this.nPillars;
    }
    
    public int getNPillars()
    {
        return this.nPillars;
    }
    
    public int getContRO()
    {
        return this.contRO;
    }
    
    public int getContPillar1()
    {
        return this.contPillar1;
    }
    
    public int getContPillar2()
    {
        return this.contPillar2;
    }
    
    public int getContPillar3()
    {
        return this.contPillar3;
    }
    
    public int getContPillar4()
    {
        return this.contPillar4;
    }
    
    public int getContPillar5()
    {
        return this.contPillar5;
    }
    
    public int getContPillar6()
    {
        return this.contPillar6;
    }
    
    public int getContPillar7()
    {
        return this.contPillar7;
    }
    
    public void setContPillar1(int contPillar1)
    {
        this.contPillar1 = contPillar1;
    }
    
    public void setContPillar2(int contPillar2)
    {
        this.contPillar2 = contPillar2;
    }
    
    public void setContPillar3(int contPillar3)
    {
        this.contPillar3 = contPillar3;
    }
    
    public void setContPillar4(int contPillar4)
    {
        this.contPillar4 = contPillar4;
    }
    
    public void setContPillar5(int contPillar5)
    {
        this.contPillar5 = contPillar5;
    }
    
    public void setContPillar6(int contPillar6)
    {
        this.contPillar6 = contPillar6;
    }
    
    public void setContPillar7(int contPillar7)
    {
        this.contPillar7 = contPillar7;
    }
    
    public boolean getBoolP1()
    {
        return this.boolP1;
    }
    
    public boolean getBoolP2()
    {
        return this.boolP2;
    }
    
    public boolean getBoolP3()
    {
        return this.boolP3;
    }
    
    public boolean getBoolP4()
    {
        return this.boolP4;
    }
    
    public boolean getBoolP5()
    {
        return this.boolP5;
    }
    
    public boolean getBoolP6()
    {
        return this.boolP6;
    }
    
    public boolean getBoolP7()
    {
        return this.boolP7;
    }
    
    public void setBoolP1(boolean boolP1)
    {
        this.boolP1 = boolP1;
    }
    
    public void setBoolP2(boolean boolP2)
    {
        this.boolP2 = boolP2;
    }
    
    public void setBoolP3(boolean boolP3)
    {
        this.boolP3 = boolP3;
    }
    
    public void setBoolP4(boolean boolP4)
    {
        this.boolP4 = boolP4;
    }
    
    public void setBoolP5(boolean boolP5)
    {
        this.boolP5 = boolP5;
    }
    
    public void setBoolP6(boolean boolP6)
    {
        this.boolP6 = boolP6;
    }
    
    public void setBoolP7(boolean boolP7)
    {
        this.boolP7 = boolP7;
    }
        
    public int posRO(String id, ArrayList<ResearchOutput> pillar)
    {
        boolean encontrado = false;
        int cont = 0;
        while ((!encontrado) && (cont != pillar.size()))
        {
            if (pillar.get(cont).getPureID().equalsIgnoreCase(id))
                encontrado = true;
            cont++;
        }
        cont--;
        if(encontrado)
            return cont;
        else
            return -1;
    }
}
