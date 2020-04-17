import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Het SNPCHeck programma download het variant_summary bestand met daarin de
 * SNP's en controleert met behulp van het variant_summary.MD5 bestand of de
 * MD5 code's overeenkomen. Hierna een door u een SNP bestand gekozen waarmee
 * er vergeleken moet worden.
 *
 * @author Robert Rijksen
 * @version 1.0.2.6
 * @since 31-01-2020
 */
public class SNPCheck {

    private static Process p;
    private static String variantMd5;
    private static JFileChooser snpFileChooser = new JFileChooser();
    private static File snpPersonFile;
    private static ArrayList<Variant> snpPersonArray = new ArrayList<>();
    private static HashMap<Integer, Variant> variantHashMap = new HashMap<>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (!variantAvailable().exists()) {
                System.out.println("Het variant_summary bestand wordt " +
                        "gedownload");
                variantDownloader();
                System.out.println("Het bestand variant_summary is gedownload" +
                        "!");
            } else {
                System.out.println("Bestand variant_summary was al gedownload");
            }
            boolean again = false;
            while (!again) {
                variantSumMd5Downloader();
                System.out.println("MD5 bestand gedownload");
                variantSumMd5Collector();
                if (!variantSumMd5Checker()) {
                    System.out.println("MD5 bestand en de MD5 van variant " +
                            "summary klopt niet");

                    variantDeleter(variantAvailable());
                    System.out.println("Het variant summary bestand wordt " +
                            "opnieuw gedownload");
                    variantDownloader();
                    System.out.println("Het variant summary bestand is " +
                            "opnieuw gedownload");
                } else {
                    again = true;
                    System.out.println("De MD5 codes komen overeen.");
                }
            }
            System.out.println("Alle snp's uit het variant summary bestand " +
                    "worden opgeslagen!");
            variantDatastructureSetter(variantSumUnzip());
            fileChooserSnp();
            snpPersonCheck(snpPersonFile);
            snpSort();
        } catch (Exception e) {
            System.out.println("Er gaat wat mis");
        }
    }

    /**
     * De functie variantAvailable controleert of het variant_summary.txt.gz
     * bestand is gedownload.
     *
     * @return Het variant_summary.txt.gz bestand
     */
    private static File variantAvailable() {
        return new File("variant_summary.txt.gz");
    }

    /**
     * De functie variantDeleter delete, als het bestand bestaat, het variant_
     * summary bestand.
     *
     * @param f1 het meegegeven variant_summary bestand.
     */
    private static void variantDeleter(File f1) {
        try {
            Files.deleteIfExists(Paths.get(f1.getAbsolutePath()));
        } catch (IOException e) {
            System.out.println("ERROR: er gaat wat mis bij het bestand" +
                    " verwijderen");
        }
    }

    /**
     * De functie variantDownloader download het variant_summary bestand.
     */
    private static void variantDownloader() {
        try {
            p = Runtime.getRuntime().exec("wget -q -N ftp://ftp.ncbi" +
                    ".nlm.nih.gov/pub/clinvar/tab_delimited/variant_summary" +
                    ".txt.gz");
            p.waitFor();
            p.destroy();
        } catch (Exception e) {
            System.out.println("ERROR " + e);
        }
    }

    /**
     * De functie variantSumUnzip unzipped het variant_summary bestand.
     *
     * @return het geunzipte variant_summary bestand.
     */
    private static File variantSumUnzip() {
        File variantSummaryUnZIP = null;
        try {
            p = Runtime.getRuntime().exec(
                    "gunzip -k variant_summary.txt.gz");
            p.waitFor();
            p.destroy();
            variantSummaryUnZIP = new File("variant_summary.txt");
        } catch (Exception e) {
            System.out.println("ERROR " + e);
        }
        return variantSummaryUnZIP;
    }

    /**
     * De functie variantSumMd5Downloader download de MD5 code die het variant_
     * summary bestand moet hebben.
     */
    private static void variantSumMd5Downloader() {
        try {
            p = Runtime.getRuntime().exec("wget -N ftp://ftp.ncbi.nl" +
                    "m.nih.gov/pub/clinvar/tab_delimited/" +
                    "variant_summary.txt.gz.md5");
            p.waitFor();
            p.destroy();
        } catch (Exception e) {
            System.out.println("ERROR " + e);
        }
    }

    /**
     * De functie variantSumMd5Collector haalt de Md5 code op van het variant_
     * summary bestand.
     */
    private static void variantSumMd5Collector() {
        String s;
        try {
            p = Runtime.getRuntime().exec("md5sum variant_summary." +
                    "txt.gz");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                variantMd5 = s.substring(0, 32);
            p.waitFor();
            p.destroy();
            System.out.println("De MD5 code van het variant summary bestand " +
                    "is: \n" + variantMd5);
        } catch (Exception e) {
            System.out.println("ERROR " + e);
        }
    }

    /**
     * De functie variantSumMd5Checker controleert of de Md5 code van het
     * variant_summary bestand gelijk is aan de Md5 code van het variant_summary
     * .MD5 bestand.
     *
     * @return een boolean met "true" als de MD5 codes overeenkomen en een
     * "false" als de MD5 codes niet overeenkomen
     */
    private static boolean variantSumMd5Checker() {
        String s;
        String newMd5 = "";
        try {
            p = Runtime.getRuntime().exec("cat variant_summary.txt.g" +
                    "z.md5");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                newMd5 = s.substring(0, 32);
            System.out.println("De MD5 code uit het variant summary.MD5 " +
                    "bestand is: \n" + newMd5);
            p.waitFor();
            p.destroy();
        } catch (Exception e) {
            System.out.println("ERROR " + e);
        }
        return newMd5.contains(variantMd5);
    }

    /**
     * De functie variantInfoSetter zet alle info uit het variant_summary
     * bestand in een Hashmap. Dit gebeurd bij het inlezen van het variant_
     * summary bestand in de functie "variantDatastructureSetter".
     *
     * @param kolom is een String[] met een regel van het variant_summary
     *              bestand als het wordt ingelezen per regel.
     */
    private static void variantInfoSetter(String[] kolom) {
        // Ik filter eerst alle rsID's eruit met een -1, leken mij geen goed
        // resultaat te geven.
        if (Integer.parseInt(kolom[9]) != -1) {
            Variant b = new Variant(Integer.parseInt(kolom[0]));
            b.setType(kolom[1]);
            b.setPosition(Integer.parseInt(kolom[19]));
            b.setPathogenicity(Integer.parseInt(kolom[7]));
            b.setGeneId(Integer.parseInt(kolom[3]));
            b.setAlternateAllele(kolom[22]);
            b.setDisease(kolom[13]);
            b.setReferenceAllele(kolom[21]);
            b.setChromosome(kolom[18]);
            b.setRsId(Integer.parseInt(kolom[9]));
            b.setAllelId(Integer.parseInt(kolom[0]));
            variantHashMap.put(b.getRsId(), b);
            // Ik heb voor een Hashmap gekozen omdat deze een big O heef van 1.
        }
    }

    /**
     * De functie variantDataStructureSetter leest het variant_summary bestand
     * in. Hierin wordt de functie "variantInfoSetter" aangeroepen om per regel
     * de informatie door te geven.
     *
     * @param f1 Het variant_summary bestand.
     */
    private static void variantDatastructureSetter(File f1) {
        try {
            BufferedReader File1 = new BufferedReader(new FileReader(f1));
            String regel;
            String[] kolom;
            while ((regel = File1.readLine()) != null) {
                kolom = regel.split("\t");
                if (kolom[16].contains("GRCh37")) {
                    variantInfoSetter(kolom);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("ERROR " + e);
        } catch (IOException b) {
            System.out.println("Er gaat wat mis met het bestand");
        }
    }

    /**
     * De functie fileChooserSnp laat een popup venster zien waarin er een
     * bestand moet worden gekozen wat onderzocht moet worden.
     */
    private static void fileChooserSnp() {
        snpFileChooser.setDialogTitle("Kies het snp bestand!");
        int returnValue = snpFileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            snpPersonFile = snpFileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(null, "Het " +
                            "bestand wordt bekeken, over enkele seconden " +
                            "worden de eerste 20 resultaten getoond!",
                    "Ter info", JOptionPane.INFORMATION_MESSAGE);

        }
    }

    /**
     * De functie snpPersonCheck leest het snpPerson bestand in. Hierin wordt
     * per regel de regel doorgegeven aan de functie snpPersonInfoSetter waarin
     * alle snp's in een Arraylist worden opgeslagen.
     * @param f2 Het gekozen Snp bestand waar de Snp's uit gehaald worden.
     */
    private static void snpPersonCheck(File f2) {
        try {
            BufferedReader File1 = new BufferedReader(new FileReader(f2));
            String regel;
            String[] kolom;
            while ((regel = File1.readLine()) != null) {
                if (!regel.startsWith("#") && regel.startsWith("rs")) {
                    kolom = regel.split("\t");
                    snpPersonInfoSetter(kolom);
                }
            }
            // Omdat er ook een onjuist bestand kan worden gekozen met de file
            // chooser heb ik ervoor gekozen om elke exception af te vangen.
        } catch (Exception e) {
            System.out.println("Er gaat wat mis met het bestand!");
        }
    }

    /**
     * De functie snpPersonInfoSetter krijgt het gekozen snp bestand per regel
     * door om zo de Snps op te slaan in een Arraylist
     * @param kolom De regel van het snp bestand
     */
    private static void snpPersonInfoSetter(String[] kolom) {
        int rsId = Integer.parseInt(kolom[0].replaceAll
                ("rs", ""));
        Variant v = variantHashMap.get(rsId);
        if (v != null) {
            // Hier wordt er gekeken of 2 phenotypes overeenkoomen, als dit het
            // geval is wordt er gekeken of 1 van de 2 overeenkomen met het
            // alternate allel van de snp van het object Variant.
            if (kolom[3].length() == 2) {
                if (kolom[3].substring(0, 1).equals(kolom[3]
                        .substring(1, 2))) {
                    if (v.getAlternateAllele().equals(kolom[3]
                            .substring(0, 1))) {
                        snpPersonArray.add(v);
                    }
                }
                // Als het maar 1 phenotype heeft, wordt er gekeken of die
                // overeenkomt met het alternate allel de snp van het object
                // variant
            } else {
                if (v.getAlternateAllele().equals(kolom[3]
                        .substring(0, 1))) {
                    snpPersonArray.add(v);
                }
            }
        }
    }

    /**
     * De functie snpSort sorteert de lijst en laat de 1e 20 resultaten zien.
     */
    private static void snpSort() {
        Collections.sort(snpPersonArray);
        System.out.println("------------------------------------------");
        for (int i = 0; i < 20; i++) {
            System.out.println(snpPersonArray.get(i));
        }
        System.out.println("------------------------------------------");
    }
}
