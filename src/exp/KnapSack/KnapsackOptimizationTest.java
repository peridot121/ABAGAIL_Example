package exp.KnapSack;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;
import opt.*;
import opt.example.CountOnesEvaluationFunction;
import opt.example.FourPeaksEvaluationFunction;
import opt.example.KnapsackEvaluationFunction;
import opt.ga.*;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.nio.file.*;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.SingleCrossOver;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.ga.UniformCrossOver;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;

/**
 *
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * modified by Yeshwant Dattatreya
 * @version 1.0
 */

class Analyze_Optimization_Test implements Runnable {

    private Thread t;

    private String problem;
    private String algorithm;
    private int iterations;
    private HashMap<String, Object> params;
    private int N;
    private int T;
    private ConcurrentHashMap<String, String> other_params;
    private int run;
    private double globalOptima;

    Analyze_Optimization_Test(
            String problem,
            String algorithm,
            int iterations,
            HashMap<String, Object> params,
            int N,
            int T,
            ConcurrentHashMap<String, String> other_params,
            int run,
            double globalOptima
    ) {
        this.problem = problem;
        this.algorithm = algorithm;
        this.iterations = iterations;
        this.params = params;
        this.N = N;
        this.T = T;
        this.other_params = other_params;
        this.run = run;
        this.globalOptima = globalOptima;
    }

    private void write_output_to_file(String output_dir, String file_name, String results, boolean final_result) {
        try {
            if (final_result) {
                String augmented_output_dir = output_dir + "/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String full_path = augmented_output_dir + "/" + file_name;
                Path p = Paths.get(full_path);
                if (Files.notExists(p)) {
                    Files.createDirectories(p.getParent());
                }
                PrintWriter pwtr = new PrintWriter(new BufferedWriter(new FileWriter(full_path, true)));
                synchronized (pwtr) {
                    pwtr.println(results);
                    pwtr.close();
                }
            }
            else {
                String full_path = output_dir + "/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "/" + file_name;
                Path p = Paths.get(full_path);
                Files.createDirectories(p.getParent());
                Files.write(p, results.getBytes());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void run() {
        try {
            EvaluationFunction ef = null;
            Distribution odd = null;
            NeighborFunction nf = null;
            MutationFunction mf = null;
            CrossoverFunction cf = null;
            Distribution df = null;
            int[] ranges;
            switch (this.problem) {
                case "knapsack":
                    ranges = new int[this.N];
                    Arrays.fill(ranges, 2);
                    double[] weights = (double[]) params.get("WEIGHTS");
                    double[] volumes = (double[]) params.get("VOLUMES");
                    double KNAPSACK_VOLUME = (double) params.get("KNAPSACK_VOLUME");
                    int[] copies = (int[]) params.get("COPIES");

                    ef = new KnapsackEvaluationFunction(weights, volumes, KNAPSACK_VOLUME, copies);
                    odd = new DiscreteUniformDistribution(ranges);
                    nf = new DiscreteChangeOneNeighbor(ranges);
                    mf = new DiscreteChangeOneMutation(ranges);
                    cf = new UniformCrossOver();
                    df = new DiscreteDependencyTree(.1, ranges);
                    break;
            }
            HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
            GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
            ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);

            String results = "";
            double optimal_value = -1;
            double start=System.currentTimeMillis(), elapsedTime = 0.0;

            switch (this.algorithm) {
                case "RHC":
                    RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);
                    for (int i = 0; i <= this.iterations; i++) {
                        results += rhc.train() + "\n";
                    }
                    optimal_value = ef.value(rhc.getOptimal());
                    break;

                case "SA55":
                    SimulatedAnnealing sa55 = new SimulatedAnnealing(
                            (double) params.get("SA55_initial_temperature"),
                            (double) params.get("SA55_cooling_factor"),
                            hcp
                    );
                    for (int i = 0; i <= this.iterations; i++) {
                        results += sa55.train() + "\n";
                    }
                    optimal_value = ef.value(sa55.getOptimal());
                    break;
                case "SA75":
                    SimulatedAnnealing sa75 = new SimulatedAnnealing(
                            (double) params.get("SA75_initial_temperature"),
                            (double) params.get("SA75_cooling_factor"),
                            hcp
                    );
                    for (int i = 0; i <= this.iterations; i++) {
                        results += sa75.train() + "\n";
                    }
                    optimal_value = ef.value(sa75.getOptimal());
                    break;
                case "SA95":
                    SimulatedAnnealing sa95 = new SimulatedAnnealing(
                            (double) params.get("SA95_initial_temperature"),
                            (double) params.get("SA95_cooling_factor"),
                            hcp
                    );
                    for (int i = 0; i <= this.iterations; i++) {
                        results += sa95.train() + "\n";
                    }
                    optimal_value = ef.value(sa95.getOptimal());
                    break;

                case "GALowPop":
                    StandardGeneticAlgorithm galow = new StandardGeneticAlgorithm(
                            ((Double) params.get("GALow_population")).intValue(),
                            ((Double) params.get("GALow_mate_number")).intValue(),
                            ((Double) params.get("GALow_mutate_number")).intValue(),
                            gap
                    );
                    for (int i = 0; i <= this.iterations; i++) {
                        results += galow.train() + "\n";
                    }
                    optimal_value = ef.value(galow.getOptimal());
                    break;

                case "GAHighPop":
                    StandardGeneticAlgorithm gahigh = new StandardGeneticAlgorithm(
                            ((Double) params.get("GAHigh_population")).intValue(),
                            ((Double) params.get("GAHigh_mate_number")).intValue(),
                            ((Double) params.get("GAHigh_mutate_number")).intValue(),
                            gap
                    );
                    for (int i = 0; i <= this.iterations; i++) {
                        results += gahigh.train() + "\n";
                    }
                    optimal_value = ef.value(gahigh.getOptimal());
                    break;

                case "MIMIC50":
                    MIMIC mimic50 = new MIMIC(
                            ((Double) params.get("MIMIC50_samples")).intValue(),
                            ((Double) params.get("MIMIC50_to_keep")).intValue(),
                            pop
                    );
                    results = "";
                    for (int i = 0; i <= this.iterations; i++) {
                        results += mimic50.train() + "\n";
                    }
                    optimal_value = ef.value(mimic50.getOptimal());
                    break;
                case "MIMIC200":
                    MIMIC mimic200 = new MIMIC(
                            ((Double) params.get("MIMIC200_samples")).intValue(),
                            ((Double) params.get("MIMIC200_to_keep")).intValue(),
                            pop
                    );
                    results = "";
                    for (int i = 0; i <= this.iterations; i++) {
                        results += mimic200.train() + "\n";
                    }
                    optimal_value = ef.value(mimic200.getOptimal());
                    break;
            }
            elapsedTime = System.currentTimeMillis() - start;

            results += "\n" +
                    "Problem: " + this.problem + "\n" +
                    "Algorithm: " + this.algorithm + "\n" +
                    "Time Elapse: " + elapsedTime +"\n"+
                    "Optimal Value: " + optimal_value + "\n"+
                    "Total function call: " + ef.getFunctionCallCount();;
            String final_result = "";
            final_result =
                    this.problem + "," +
                            this.algorithm + "," +
                            this.N + "," +
                            this.iterations + "," +
                            this.run + "," +
                            ef.getFunctionCallCount() + "," +
                            optimal_value + "," +
                            elapsedTime + "," +
                            this.globalOptima;
            write_output_to_file(this.other_params.get("output_folder"), "final_results.csv", final_result, true);
            String file_name =
                    this.problem + "_" + this.algorithm + "_N_" + this.N +
                            "_iter_" + this.iterations + "_run_" + this.run + ".csv";
            write_output_to_file(this.other_params.get("output_folder"), file_name, results, false);
            System.out.println(results);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start () {
        if (t == null)
        {
            t = new Thread (this);
            t.start ();
        }
    }
}


public class KnapsackOptimizationTest {

    public static void main(String[] args) {

        ConcurrentHashMap<String, String> other_params = new ConcurrentHashMap<>();
        other_params.put("output_folder","Optimization_Results-Knapsack");
        int num_runs = 10;

        int[] N = new int[] {100};
        int[] iterations = {1, 50, 100, 150, 200, 300, 400, 500, 600, 700, 800, 900, 1000};
        String[] algorithms = {"RHC", "SA55", "SA75", "SA95", "GALowPop", "GAHighPop", "MIMIC50", "MIMIC200"};

        for (int i = 0; i < algorithms.length; i++) {
            for (int j = 0; j < N.length; j++) {
                //Create a new Knapsack problem for each N
                Random random = new Random();
                int NUM_ITEMS = N[j];
                int COPIES_EACH = NUM_ITEMS/10;
                double MAX_WEIGHT = 50;
                double MAX_VOLUME = 50;
                double KNAPSACK_VOLUME =
                        MAX_VOLUME * NUM_ITEMS * COPIES_EACH * .4;
                double[] weights = new double[NUM_ITEMS];
                double[] volumes = new double[NUM_ITEMS];
                int[] copies = new int[NUM_ITEMS];
                double globalOptima = 0;
                Arrays.fill(copies, COPIES_EACH);
                for (int k = 0; k < NUM_ITEMS; k++) {
                    weights[k] = random.nextDouble() * MAX_WEIGHT;
                    volumes[k] = random.nextDouble() * MAX_VOLUME;
                    globalOptima += weights[k];
                }

                System.out.println("Global optima: " + globalOptima);

                //Knapsack Test
                HashMap<String, Object> knapsack_params = new HashMap<>();
                knapsack_params.put("SA55_initial_temperature",1E11);
                knapsack_params.put("SA55_cooling_factor",.55);
                knapsack_params.put("SA75_initial_temperature",1E11);
                knapsack_params.put("SA75_cooling_factor",.75);
                knapsack_params.put("SA95_initial_temperature",1E11);
                knapsack_params.put("SA95_cooling_factor",.95);
                knapsack_params.put("GAHigh_population",600.);
                knapsack_params.put("GAHigh_mate_number",400.);
                knapsack_params.put("GAHigh_mutate_number",60.);
                knapsack_params.put("GALow_population",100.);
                knapsack_params.put("GALow_mate_number",75.);
                knapsack_params.put("GALow_mutate_number",10.);
                knapsack_params.put("MIMIC200_samples",200.);
                knapsack_params.put("MIMIC200_to_keep",100.);
                knapsack_params.put("MIMIC50_samples",50.);
                knapsack_params.put("MIMIC50_to_keep",5.);
                knapsack_params.put("WEIGHTS", weights);
                knapsack_params.put("VOLUMES", volumes);
                knapsack_params.put("KNAPSACK_VOLUME", KNAPSACK_VOLUME);
                knapsack_params.put("COPIES", copies);

                for (int k = 0; k < iterations.length; k++) {
                    for (int l = 0; l < num_runs; l++) {
                        new Analyze_Optimization_Test(
                                "knapsack",
                                algorithms[i],
                                iterations[k],
                                knapsack_params,
                                N[j],
                                N[j]/10,
                                other_params,
                                l,
                                globalOptima
                        ).start();
                    }
                }
            }
        }
    }
}

