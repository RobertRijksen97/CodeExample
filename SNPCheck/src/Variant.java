
/**
 * De Variant.java Class slaat alle varianten op uit het variant_summary
 * bestand. Deze Class wordt gebruikt in de Class SNPCheck.java.
 *
 * @author  Robert Rijksen
 * @version 1.3.8
 * @since   31-01-2020
 */

public class Variant implements Comparable<Variant> {

    private int allelId;
    private String type;
    private int position;
    private int pathogenicity;
    private int geneId;
    private String alternateAllele;
    private String disease;
    private String referenceAllele;
    private String chromosome;
    private int numberVariant;
    private int rsId;

    /**
     * In deze Variant functie wordt het object aangemaakt.
     * @param allelId Deze variant wordt als unieke code de allelId meegegeven.
     */

    public Variant(int allelId) {
        this.allelId = allelId;
    }

    /**
     * Deze functie slaat de AllelId op
     * @param allelId Het allelId van de snp variant.
     */
    public void setAllelId(int allelId) {
        this.allelId = allelId;
    }

    /**
     * Deze functie haalt het allelId op van de Variant.
     * @return Het allelId van de snp variant.
     */
    public int getAllelId() {
        return allelId;
    }

    /**
     * Deze functie haalt het type op van de variant.
     * @return Het type van de snp variant.
     */
    public String getType() {
        return type;
    }

    /**
     * Deze functie slaat het type van de variant op.
     * @param type Het type van de snp variant.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Deze functie haalt de posite van de variant op.
     * @return De positie van de snp variant.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Deze functie slaat de positie van de variant op.
     * @param position = De positie van de snp variant.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Deze functie haalt de pathogeniciteit op van de snp variant.
     * @return De pathogeniciteit van de snp variant.
     */
    public int getPathogenicity() {
        return pathogenicity;
    }

    /**
     * Deze functie slaat de pathogeniciteit op van de snp variant.
     * @param pathogenicity = De pathogeniciteit van de snp variant.
     */
    public void setPathogenicity(int pathogenicity) {
        this.pathogenicity = pathogenicity;
    }

    /**
     * Deze functie haalt het genID op van de snp variant.
     * @return Het genID van de snp variant.
     */
    public int getGeneId() {
        return geneId;
    }

    /**
     * Deze functie slaat het genID op van de snp variant.
     * @param geneId = Het genID van de snp variant.
     */
    public void setGeneId(int geneId) {
        this.geneId = geneId;
    }

    /**
     * Deze functie haalt het alternate allel op van de snp variant.
     * @return Het alternate allel van de snp variant.
     */
    public String getAlternateAllele() {
        return alternateAllele;
    }

    /**
     * Deze functie slaat het alternate allel op van de snp variant.
     * @param alternateAllele = Het alternate allel van de snp variant.
     */
    public void setAlternateAllele(String alternateAllele) {
        this.alternateAllele = alternateAllele;
    }

    /**
     * Deze functie haalt de ziekte op van de snp variant.
     * @return De ziekte van de snp variant.
     */
    public String getDisease() {
        return disease;
    }

    /**
     * Deze functie slaat de ziekte op van de snp variant.
     * @param disease = De ziekte van de snp variant.
     */
    public void setDisease(String disease) {
        this.disease = disease;
    }

    /**
     * Deze functie haalt het reference allel op van de snp variant.
     * @return Het reference allel van de snp variant.
     */
    public String getReferenceAllele() {
        return referenceAllele;
    }

    /**
     * Deze functie slaat het reference allel op van de snp variant.
     * @param referenceAllele = het reference allel van de snp variant.
     */
    public void setReferenceAllele(String referenceAllele) {
        this.referenceAllele = referenceAllele;
    }

    /**
     * Deze functie haalt het chromosoom op van de snp variant.
     * @return Het chromosoom van de snp variant.
     */
    public String getChromosome() {
        return chromosome;
    }

    /**
     * Deze functie slaat het chromosoomo op van de snp variant.
     * @param chromosome = Het chromosoom van de snp variant.
     */
    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    /**
     * Deze functie geeft het nummer van de snp variant.
     * @return Het nummer van de snp variant.
     */
    public int getNumberVariant() {
        return numberVariant;
    }

    /**
     * Deze functie slaat het nummer op  van de snp variant.
     * @param numberVariant = Het nummer van de variant.
     */
    public void setNumberVariant(int numberVariant) {
        this.numberVariant = numberVariant;
    }

    /**
     * Deze functie haalt de rsID op van de snp variant.
     * @return De rsID van de snp variant.
     */
    public int getRsId() {
        return rsId;
    }

    /**
     * Deze functie slaat het rsID op van de snp variant.
     * @param rsId = Het rsID van de snp variant.
     */
    public void setRsId(int rsId) {
        this.rsId = rsId;
    }

    /**
     * Deze Override van de tostring methode laat informatie zien als er een
     * variant object wordt aangeroepen.
     * @return De pathogeniciteit met daarbij het chromosoom en de daarbij
     * horende positie op het chormosoom van de snp variant.
     */
    @Override
    public String toString() {
        return "Pathogenicity: " + getPathogenicity() + " Chr.: " +
                getChromosome() + " Position: " + getPosition();
    }

    /**
     * Deze override van de compareTo methode zet de de variant objecten op
     * volgorde van pathogeniciteit, daarna op volgorde van chromosoom en daarna
     * op volgorde van de positie op het chromosoom.
     * @param o Het object waarmee de compareTo de op volgorde maakt.
     * @return De goede volgorde van de pathogeniciteit, chromosoom en positie
     * op het chromosoom.
     */
    @Override
    public int compareTo(Variant o) {
        int i = Integer.compare(o.pathogenicity, pathogenicity);
        if (i != 0) return i;
        try {
            // Omdat er nummers en letters als chromosoom opgeslagen zijn,
            // heb ik ervoor gekozen om eerst de nummers op volgorde te zetten
            // en als dit zorgde voor een NumberFormatException om dat ik de
            // String parse naar Int, wordt deze afgevangen en gewoon gezien als
            // String.
            i = Integer.compare(Integer.parseInt(chromosome),
                    Integer.parseInt(o.chromosome));
            if (i != 0) return i;
        } catch (NumberFormatException e) {
            i = chromosome.compareTo(o.chromosome);
            if (i != 0) return i;
        }
        return Integer.compare(position, o.position);
    }
}
