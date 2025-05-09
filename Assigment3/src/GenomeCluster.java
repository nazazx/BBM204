import java.util.*;

public class GenomeCluster {
    public Map<String, Genome> genomeMap = new HashMap<>();

    public void addGenome(Genome genome) {
        // TODO: Add genome to the cluster
        genomeMap.put(genome.id,genome);
    }

    public boolean contains(String genomeId) {
        // TODO: Return true if the genome is in the cluster
        if (genomeMap.containsKey(genomeId)){
            return true;
        }
        return false;
    }

    public Genome getMinEvolutionGenome() {
        // TODO: Return the genome with minimum evolutionFactor
        int min=Integer.MAX_VALUE;
        Genome minGenome=null;
        for (Genome genome:genomeMap.values()){
            if (genome.evolutionFactor<min){
                minGenome=genome;
                min=genome.evolutionFactor;

            }
        }
        return minGenome;
    }

    public int dijkstra(String startId, String endId) {
        // TODO: Implement Dijkstra's algorithm to return shortest path
        Map<String, Integer> costs = new HashMap<>();
         for (Genome genome:genomeMap.values()){
             costs.put(genome.id,Integer.MAX_VALUE);
         }

        PriorityQueue<String> queue=new PriorityQueue<>(Comparator.comparing(costs::get));
        costs.put(startId,0);
        queue.offer(startId);

        while (!queue.isEmpty()){
            String current_id=queue.poll();
            int current_cost=costs.get(current_id);
            Genome genome=genomeMap.get(current_id);

            if (current_id.equals(endId)){
                return current_cost;
            }

            for (Genome.Link link :genome.links){
                String neighbor=link.target;
                int adaptation=link.adaptationFactor;
                int value=current_cost+adaptation;
                if (value<costs.get(neighbor)){
                    costs.put(neighbor,value);
                    queue.offer(neighbor);
                }
            }
        }

        return -1;
    }
}
