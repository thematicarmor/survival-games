package supercoder79.survivalgames.game.map.biome.blend;

import supercoder79.survivalgames.game.map.biome.BiomeGen;

public final class LinkedBiomeWeightMap {
    private BiomeGen biome;
    private double[] weights;
    private LinkedBiomeWeightMap next;
    
    public LinkedBiomeWeightMap(BiomeGen biome, LinkedBiomeWeightMap next) {
        this.biome = biome;
        this.next = next;
    }
    
    public LinkedBiomeWeightMap(BiomeGen biome, int chunkColumnCount, LinkedBiomeWeightMap next) {
        this.biome = biome;
        this.weights = new double[chunkColumnCount];
        this.next = next;
    }
    
    public BiomeGen getBiome() {
        return biome;
    }
    
    public double[] getWeights() {
        return weights;
    }
    
    public void setWeights(double[] weights) {
        this.weights = weights;
    }
    
    public LinkedBiomeWeightMap getNext() {
        return next;
    }
}