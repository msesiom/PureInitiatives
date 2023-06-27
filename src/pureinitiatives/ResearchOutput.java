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
public class ResearchOutput {
    
    private String pureID, title, resumen;
//    private boolean pillar1, pillar2, pillar3, pillar4, pillar5, pillar6, pillar7;
    
    public ResearchOutput(String pureID)
    {
        this.pureID = pureID;
        this.title = "";
        this.resumen = "";
//        this.pillar1 = false;
//        this.pillar2 = false;
//        this.pillar3 = false;
//        this.pillar4 = false;
//        this.pillar5 = false;
//        this.pillar6 = false;
//        this.pillar7 = false;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public void setAbstract(String resumen)
    {
        this.resumen = resumen;
    }
    
    public String getText()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(title);
        sb.append(" ");
        sb.append(resumen);
        return sb.toString();
    }
    
    public String getPureID()
    {
        return this.pureID;
    }
    
//    public void setPillar1(boolean pillar)
//    {
//        this.pillar1 = pillar;
//    }
//    
//    public void setPillar2(boolean pillar)
//    {
//        this.pillar2 = pillar;
//    }
//    
//    public void setPillar3(boolean pillar)
//    {
//        this.pillar3 = pillar;
//    }
//    
//    public void setPillar4(boolean pillar)
//    {
//        this.pillar4 = pillar;
//    }
//    
//    public void setPillar5(boolean pillar)
//    {
//        this.pillar5 = pillar;
//    }
//    
//    public void setPillar6(boolean pillar)
//    {
//        this.pillar6 = pillar;
//    }
//    
//    public void setPillar7(boolean pillar)
//    {
//        this.pillar7 = pillar;
//    }
}
