import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.*;

public class AlienFlora {
    private File xmlFile;
    private ArrayList<Genome> genoms = new ArrayList<>();
    private ArrayList<GenomeCluster> genomeClusters=new ArrayList<>();

    public AlienFlora(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    public void readGenomes() throws Exception {
        // TODO:
        // - Parse XML
        // - Read genomes and links
        // - Create clusters
        // - Print number of clusters and their genome IDs
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        DocumentBuilder builder= factory.newDocumentBuilder();
        Document document=builder.parse(xmlFile);

        NodeList genomlist=document.getElementsByTagName("genome");
        for (int i=0;i<genomlist.getLength();i++){
            Node node=genomlist.item(i);
            String id =((Element) node).getElementsByTagName("id").item(0).getTextContent();
            int evolation_factor =Integer.parseInt(((Element) node).getElementsByTagName("evolutionFactor").item(0).getTextContent());
            Genome genome=new Genome(id,evolation_factor);

            NodeList linksNodes=((Element)node).getElementsByTagName("link");

            for (int j=0;j<linksNodes.getLength();j++){
                Node link_node=linksNodes.item(j);
                String Target_id =((Element) link_node).getElementsByTagName("target").item(0).getTextContent();
                int adaptation_factor =Integer.parseInt(((Element) link_node).getElementsByTagName("adaptationFactor").item(0).getTextContent());
                genome.addLink(Target_id,adaptation_factor);
            }
            genoms.add(genome);
        }
        Set<String> marked=new HashSet<>();

        for (Genome genome:genoms){
            String id=genome.id;
            if (!marked.contains(id)){
                GenomeCluster cluster=new GenomeCluster();
                dfs(marked,id,cluster);
                genomeClusters.add(cluster);
            }
        }
        System.out.println("##Start Reading Flora Genomes##");
        System.out.println("Number of Genome Clusters: " + genomeClusters.size());

        ArrayList<ArrayList<String>> cluster_ids=new ArrayList<>();
        for (GenomeCluster cluster:genomeClusters){
            ArrayList<String> ids=new ArrayList<>(cluster.genomeMap.keySet());
            Collections.sort(ids);
            cluster_ids.add(ids);
        }
        System.out.println("For the Genomes: " + cluster_ids);
        System.out.println("##Reading Flora Genomes Completed##");

    }
    private Genome find_genom_by_id(String id){
        for (int i=0;i<genoms.size();i++){
            if (genoms.get(i).id.equals(id)){
                return genoms.get(i);
            }
        }
        return null;
    }
    private void dfs(Set<String> marked,String current,GenomeCluster genomeCluster){
        marked.add(current);
        Genome Current_genome=find_genom_by_id(current);
        if (Current_genome==null){
            return;
        }
        if (!genomeCluster.contains(current)){
            genomeCluster.addGenome(Current_genome);
        }

        for (Genome.Link neighbor_link: Current_genome.links){
            String neigbor_id=neighbor_link.target;
            if (!marked.contains(neigbor_id)){
                dfs(marked,neigbor_id,genomeCluster);
            }
        }
    }

    public void evaluateEvolutions() throws Exception{
        // TODO:
        // - Parse and process possibleEvolutionPairs
        // - Find min evolution genome in each cluster
        // - Calculate and print evolution factors
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        NodeList nodes=document.getElementsByTagName("pair");

        ArrayList<Double> results =new ArrayList<>();
        int certified= 0;

        for (int i=0;i<nodes.getLength();i++){
            Node pair=nodes.item(i);
            if (pair.getParentNode().getNodeName().equals("possibleEvolutionPairs")){
                Element element_pair=(Element) pair;
                String first_id=element_pair.getElementsByTagName("firstId").item(0).getTextContent();
                String second_id=element_pair.getElementsByTagName("secondId").item(0).getTextContent();
                double result=0;
                if (find_cluster(first_id)==find_cluster(second_id)){
                    result=-1;
                }
                else{
                    GenomeCluster first_cluster=find_cluster(first_id);
                    GenomeCluster second_cluster=find_cluster(second_id);
                    result=(first_cluster.getMinEvolutionGenome().evolutionFactor+second_cluster.getMinEvolutionGenome().evolutionFactor)/2.0;
                    certified++;
                }
                results.add(result);

            }
        }
        System.out.println("##Start Evaluating Possible Evolutions##");
        System.out.println("Number of Possible Evolutions: " + results.size());
        System.out.println("Number of Certified Evolution: " + certified);

        System.out.println("Evolution Factor for Each Evolution Pair: " + Arrays.toString(results.toArray()));
        System.out.println("##Evaluated Possible Evolutions##");
    }
    private GenomeCluster find_cluster(String id){
        for (GenomeCluster cluster:genomeClusters){
            if(cluster.contains(id)){
                return cluster;
            }

        }
        return null;
    }
    public void evaluateAdaptations() throws Exception{
        // TODO:
        // - Parse and process possibleAdaptationPairs
        // - If genomes in same cluster, use Dijkstra to calculate min path
        // - Print adaptation factors

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        NodeList nodes=document.getElementsByTagName("pair");
        int certified=0;
        ArrayList<Integer> results =new ArrayList<>();

        for (int i=0;i<nodes.getLength();i++){
            Node pair=nodes.item(i);
            if (pair.getParentNode().getNodeName().equals("possibleAdaptationPairs")){
                Element element_pair=(Element) pair;
                String first_id=element_pair.getElementsByTagName("firstId").item(0).getTextContent();
                String second_id=element_pair.getElementsByTagName("secondId").item(0).getTextContent();
                int res=0;
                GenomeCluster cluster1 = find_cluster(first_id);
                GenomeCluster cluster2 = find_cluster(second_id);

                if (cluster1==null || cluster2==null){
                    res=-1;
                }
                else if(cluster1!=cluster2){
                    res=-1;
                }
                else{
                    res = cluster1.dijkstra(first_id, second_id);
                    if (res != -1){
                        certified++;
                    }
                }
                results.add(res);
            }
        }

        System.out.println("##Start Evaluating Possible Adaptations##");
        System.out.println("Number of Possible Adaptations: " + results.size());
        System.out.println("Number of Certified Adaptations: " + certified);
        System.out.println("Adaptation Factor for Each Adaptation Pair: " + Arrays.toString(results.toArray()));
        System.out.println("##Evaluated Possible Adaptations##");
    }

}
