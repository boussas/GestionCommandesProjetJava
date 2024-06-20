//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.File;
import java.util.*;
public class Main {
    public static void main(String[] args) {

        String rep="c:/file.txt";
        File f=new File(rep);
        if(f.exists()){
            String[] contenu=f.list();
            for(int i=0;i<contenu.length;i++){
                File f2=new File(rep,contenu[i]);
                if(f2.isDirectory())
                    System.out.println("REP:"+contenu[i]);
                else
                    System.out.println("Fichier:"+contenu[i]+"Size:"
                            +contenu[i].length());}}

        else{
            System.out.println(rep+" n'existe pas");}

    }
}