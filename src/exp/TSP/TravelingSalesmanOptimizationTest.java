package exp.TSP;

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
import dist.DiscretePermutationDistribution;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

import opt.*;
import opt.example.*;
import opt.ga.*;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.Instance;

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
    double[][]points;

    Analyze_Optimization_Test(
            String problem,
            String algorithm,
            int iterations,
            HashMap<String, Object> params,
            int N,
            int T,
            ConcurrentHashMap<String, String> other_params,
            int run,
            double[][]points
    ) {
        this.problem = problem;
        this.algorithm = algorithm;
        this.iterations = iterations;
        this.params = params;
        this.N = N;
        this.T = T;
        this.other_params = other_params;
        this.run = run;
        this.points = points;
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
            //EvaluationFunction ef = null;
            TravelingSalesmanEvaluationFunction ef = null;
            Distribution odd = null;
            NeighborFunction nf = null;
            MutationFunction mf = null;
            CrossoverFunction cf = null;
            Distribution df = null;
            int[] ranges;
            //switch (this.problem) {
            //    case "traveling_salesman":
            ranges = new int[this.N];
            Arrays.fill(ranges, 2);
            ef = new TravelingSalesmanRouteEvaluationFunction(this.points);
            odd = new DiscretePermutationDistribution(this.N);
            nf = new SwapNeighbor();
            mf = new SwapMutation();
            cf = new TravelingSalesmanCrossOver(ef);
            df = new DiscreteDependencyTree(.1, ranges);
            //        break;
            //}
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

                case "MIMIC20":
                    MIMIC mimic20 = new MIMIC(
                            ((Double) params.get("MIMIC20_samples")).intValue(),
                            ((Double) params.get("MIMIC20_to_keep")).intValue(),
                            pop
                    );
                    results = "";
                    for (int i = 0; i <= this.iterations; i++) {
                        results += mimic20.train() + "\n";
                    }
                    optimal_value = ef.value(mimic20.getOptimal());
                    break;

                case "MIMIC5":
                    MIMIC mimic5 = new MIMIC(
                            ((Double) params.get("MIMIC5_samples")).intValue(),
                            ((Double) params.get("MIMIC5_to_keep")).intValue(),
                            pop
                    );
                    results = "";
                    for (int i = 0; i <= this.iterations; i++) {
                        results += mimic5.train() + "\n";
                    }
                    Instance testval = mimic5.getOptimal();
                    optimal_value = ef.value(testval);
                    break;
                default:
                    int foo = 1;
            }
            elapsedTime = System.currentTimeMillis() - start;
            results += "\n" +
                    "Problem: " + this.problem + "\n" +
                    "Algorithm: " + this.algorithm + "\n" +
                    "Time Elapse: " + elapsedTime +"\n"+
                    "Optimal Value: " + optimal_value + "\n" +
                    "Total function call: " + ef.getFunctionCallCount();
            String final_result = "";
            final_result =
                    this.problem + "," +
                            this.algorithm + "," +
                            this.N + "," +
                            this.iterations + "," +
                            this.run + "," +
                            ef.getFunctionCallCount() + "," +
                            optimal_value + "," +
                            elapsedTime;
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


public class TravelingSalesmanOptimizationTest {

    public static void main(String[] args) {

        ConcurrentHashMap<String, String> other_params = new ConcurrentHashMap<>();
        other_params.put("output_folder","Optimization_Results-Traveling-Salesman");
        int num_runs = 10;

        //String[] algorithms = {"MIMIC20", "MIMIC5", "RHC", "SA55", "SA75", "SA95", "GALowPop", "GAHighPop" };
        String[] algorithms = {"MIMIC20", "MIMIC5"};

        //Traveling Salesman Test
        HashMap<String, Object> traveling_salesman_test_params = new HashMap<>();
        traveling_salesman_test_params.put("SA55_initial_temperature",1E11);
        traveling_salesman_test_params.put("SA75_initial_temperature",1E11);
        traveling_salesman_test_params.put("SA95_initial_temperature",1E11);
        traveling_salesman_test_params.put("SA55_cooling_factor",.55);
        traveling_salesman_test_params.put("SA75_cooling_factor",.75);
        traveling_salesman_test_params.put("SA95_cooling_factor",.95);
        traveling_salesman_test_params.put("GAHigh_population",600.);
        traveling_salesman_test_params.put("GAHigh_mate_number",400.);
        traveling_salesman_test_params.put("GAHigh_mutate_number",60.);
        traveling_salesman_test_params.put("GALow_population",100.);
        traveling_salesman_test_params.put("GALow_mate_number",75.);
        traveling_salesman_test_params.put("GALow_mutate_number",10.);
        traveling_salesman_test_params.put("MIMIC20_samples",20.);
        traveling_salesman_test_params.put("MIMIC20_to_keep",2.);
        traveling_salesman_test_params.put("MIMIC5_samples",5.);
        traveling_salesman_test_params.put("MIMIC5_to_keep",1.);

        int[] N = {50};
        int[] iterations = { 1, 500, 1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000 };
        for (int i = 0; i < algorithms.length; i++) {
            for (int j = 0; j < N.length; j++) {
                double[][] points = new double[N[j]][2];
                Random random = new Random();
                for (int z = 0; z < points.length; z++) {
                    points[z][0] = random.nextDouble();
                    points[z][1] = random.nextDouble();
                }
                for (int k = 0; k < iterations.length; k++) {
                    for (int l = 0; l < num_runs; l++) {
                        new Analyze_Optimization_Test(
                                "traveling_salesman",
                                algorithms[i],
                                iterations[k],
                                traveling_salesman_test_params,
                                N[j],
                                N[j]/10,
                                other_params,
                                l,
                                points
                        ).start();
                    }
                }
            }
        }
    }
}
