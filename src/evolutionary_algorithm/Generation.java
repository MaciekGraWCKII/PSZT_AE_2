package evolutionary_algorithm;

import java.util.Random;

import alphabet.Alphabet;
import alphabet.UnknownCharacterException;

public class Generation
{
    /**Used by this class.*/
    protected Alphabet alphabet;
    /***/
    protected Random rand;
    /**Cipher to be broken*/
    protected Specimen exemplar;
    /**Encoded message*/
    protected int[] cryptogram;
    /***/
    protected String message;
    /**Currently evaluated and bred group of Specimens.*/
    protected EvaluatedSpecimen[] population;
    /**Result of <i>population</i> breeding*/
    protected EvaluatedSpecimen[] children;
    /**Will become <i>population</i> in the next iteration of the algorithm*/
    protected EvaluatedSpecimen[] nextGeneration;
    /**populationSize will be constant throughout generations*/
    protected int populationSize;
    /**desiredEvaluation to be achieved. This algorithm WILL STOP when one of the specimens is evaluated with value greater or equal to this parameter. [0.0d ; 1.0d]*/
    protected double desiredEvaluation;
    /**maxNumberOfIterations to be processed. This algorithm will stop when this value is met regardless of current specimens evaluations.*/
    protected int maxNumberOfIterations;
    /**maxPercentOfOldGenerationSurvivors who can transit to the next generation. [0.0d ; 1.0d]*/
    protected double percentageOfOldGenerationSurvivors;
    /**maxPercentOfParents how many specimens can participate in reproduction. [0.0d ; 1.0d]*/
    protected double percentageOfParents;
    /**how many Specimens can participate in reproduction in each iteration. Should be even.*/
    protected int numberOfParents;
    /**mutationChance with which each child is subject to mutation. [0.0d ; 1.0d]*/
    protected double mutationChance;
    
    /**
     * @param alphabet to be used
     * @param rand for randomization
     */
    public Generation(final Alphabet alphabet, final Random rand)
    {
        this.alphabet = alphabet;
        this.rand = rand;
    }
    
    /**
     * Will attempt to break the substitution cipher by means of an evolutionary algorithm.
     * 
     * @param populationSize will be constant throughout generations
     * @param desiredEvaluation to be achieved. This algorithm WILL STOP when one of the specimens is evaluated with value greater or equal to this parameter. [0.0d ; 1.0d]
     * @param maxNumberOfIterations to be processed. This algorithm will stop when this value is met regardless of current specimens evaluations.
     * @param percentageOfOldGenerationSurvivors who can transit to the next generation. [0.0d ; 1.0d]
     * @param percentageOfParents how many specimens can participate in reproduction. [0.0d ; 1.0d]
     * @param mutationChance with which each child is subject to mutation. [0.0d ; 1.0d]
     * @param message to be used in the process
     * @param statistics to receive the message deciphered by the best specimen in the generation, his evaluation, mean evaluation of 
     *      the generation and the worst evaluation
     *
     * @exception IncorrectDesiredEvaluation when the 'desiredEvaluation' parameter does not belong to [0.0d ; 1.0d]
     * @exception IncorrectPercentageOfOldGenerationSurvivors when the 'percentageOfOldGenerationSurvivors' parameter does not belong to [0.0d ; 1.0d]
     * @exception IncorrectPercentageOfParents when the 'percentageOfParents' parameter does not belong to [0.0d ; 1.0d] 
     * @exception IncorrectMutationChanceException when the 'mutationChance' parameter does not belong to [0.0d ; 1.0d]
     * @exception UnknownCharacterException if an unknown character is found in the provided <i>text</i>.
     * This character will be stored as a description of the Exception.
     */
    public void evolve(final int populationSize, final double desiredEvaluation, 
            final int maxNumberOfIterations, final double percentageOfOldGenerationSurvivors, 
            final double percentageOfParents, final double mutationChance, final String message, final Statistics statistics)
            throws IncorrectDesiredEvaluation, IncorrectPercentageOfOldGenerationSurvivors, IncorrectPercentageOfParents, 
            IncorrectMutationChanceException, UnknownCharacterException
    {
        setParameters(populationSize, desiredEvaluation, maxNumberOfIterations, 
                percentageOfOldGenerationSurvivors, percentageOfParents, mutationChance, message);
        checkParameters();
        prepareDataStructure();
        while(checkExitConditions())
        {
            evaluatePopulation();
            generateStatistics(statistics);
            performReproduction();
        }
    }
    
    /**
     * Calculates and sends the message deciphered by the best specimen in the generation, his evaluation, mean evaluation of 
     * the generation and the worst evaluation to the <i>statistics</i>.
     * 
     * @param statistics to receive data
     */
    protected void generateStatistics(final Statistics statistics)
    {
        //TODO
    }
    
    /**
     * Will store given parameters.
     * 
     * @param populationSize
     * @param desiredEvaluation
     * @param maxNumberOfIterations
     * @param maxPercentOfOldGenerationSurvivors
     * @param maxPercentOfParents
     * @param mutationChance
     * @param message
     */
    protected void setParameters(final int populationSize, final double desiredEvaluation, 
            final int maxNumberOfIterations, final double percentageOfOldGenerationSurvivors, 
            final double percentageOfParents, final double mutationChance, final String message)
    {
        this.populationSize = populationSize;
        this.desiredEvaluation = desiredEvaluation;
        this.maxNumberOfIterations = maxNumberOfIterations;
        this.percentageOfOldGenerationSurvivors = percentageOfOldGenerationSurvivors;
        this.percentageOfParents = percentageOfParents;
        this.numberOfParents = (int)(populationSize * percentageOfParents);
        if(numberOfParents % 2 == 1)
        {
            ++numberOfParents;
        }
        this.mutationChance = mutationChance;
        this.message = message;
    }
    
    /**
     * Will check if parameters are correct.
     * 
     * @exception IncorrectDesiredEvaluation when the 'desiredEvaluation' parameter does not belong to [0.0d ; 1.0d]
     * @exception IncorrectMaxPercentOfOldGenerationSurvivors when the 'maxPercentOfOldGenerationSurvivors' parameter does not belong to [0.0d ; 1.0d]
     * @exception IncorrectpercentageOfParents when the 'maxPercentOfParents' parameter does not belong to [0.0d ; 1.0d] 
     * @exception IncorrectMutationChanceException when the 'mutationChance' parameter does not belong to [0.0d ; 1.0d]
     */
    protected void checkParameters() 
            throws IncorrectDesiredEvaluation, IncorrectPercentageOfOldGenerationSurvivors, 
            IncorrectPercentageOfParents, IncorrectMutationChanceException
    {
        if(desiredEvaluation < 0.0d && desiredEvaluation > 1.0d)
        {
            throw new IncorrectDesiredEvaluation();
        }
        
        if(percentageOfOldGenerationSurvivors < 0.0d && percentageOfOldGenerationSurvivors > 1.0d)
        {
            throw new IncorrectPercentageOfOldGenerationSurvivors();
        }
        
        if(percentageOfParents < 0.0d && percentageOfParents > 1.0d)
        {
            throw new IncorrectPercentageOfParents();
        }
        
        if(mutationChance < 0.0d && mutationChance > 1.0d)
        {
            throw new IncorrectMutationChanceException();
        }
        
    }
    
    /**
     * Everything necessary before the start of actual algorithm.
     * 
     * @throws UnknownCharacterException if an unknown character is found in the provided <i>text</i>.
     * This character will be stored as a description of the Exception.
     */
    protected void prepareDataStructure() throws UnknownCharacterException
    {
        generateCipher();
        cipherMessage();
        createStructure();
        generatePopulation();
    }
    
    /**
     * Will create the specimen whose cipher is to be cracked.
     */
    protected void generateCipher()
    {
        exemplar = new Specimen(alphabet.getAlphabetLength(), rand);
    }
    
    /**
     * Will cipher the message.
     * 
     * @throws UnknownCharacterException if an unknown character is found in the provided <i>text</i>.
     * This character will be stored as a description of the Exception.
     */
    protected void cipherMessage() throws UnknownCharacterException
    {
        cryptogram = alphabet.convert(message);
        exemplar.encode(cryptogram);
    }
    
    /**
     * Will create data structures used by the algorithm.
     */
    protected void createStructure()
    {
        population = new EvaluatedSpecimen[populationSize];
        children = new EvaluatedSpecimen[populationSize];
        nextGeneration = new EvaluatedSpecimen[populationSize];
    }
    
    /**
     * Will create population of specimens destined to break the substitution cipher.
     */
    protected void generatePopulation()
    {
        for(int i = 0; i < populationSize; ++i)
        {
            try
            {
                population[i] = new EvaluatedSpecimen(new Specimen(alphabet.getAlphabetLength(), rand), exemplar);
            } 
            catch (IncompatibleSpecimensException e)
            {
                //If we find ourselves stranded here, we are unable to recover.
                throw new RuntimeException();
            }
        }
    }
    
    /**
     * Handles the process of reproduction:
     * -selection of specimens for mating
     * -generation of children and mutations
     * -evaluates children
     * -determines specimens which will create the next generation
     * -removes dead specimens
     */
    protected void performReproduction()
    {
        //TODO
    }
    
    /** 
     * @return whether the algorithms exit conditions are met
     */
    protected boolean checkExitConditions()
    {
        //TODO
        return false;
    }
    
    /**
     * Evaluates all specimens.
     */
    public void evaluatePopulation()
    {
        //TODO
    }
    
    protected class EvaluatedSpecimen
    {
        /***/
        protected Specimen specimen;
        /***/
        protected double evaluation;
        
        /**
         * @param specimen to be stored 
         * @param Exemplar to be compared with the stored specimen
         * 
         * @throws IncompatibleSpecimensException when the <i>expemplar</i> can not be compared with this Specimen.
         */
        public EvaluatedSpecimen(final Specimen specimen, final Specimen exemplar) throws IncompatibleSpecimensException
        {
            assert(specimen != null && exemplar != null);
            this.specimen = specimen;
            reevaluate(exemplar);
        }
        
        /**
         * Will return currently held evaluation.<br>
         * 
         * @return percentage of sameness with the exemplar. [0.0d ; 1.0d]
         */
        public double getEvaluation()
        {
            return evaluation;
        }
        
        /**
         * @return
         */
        public Specimen getSpecimen()
        {
            return specimen;
        }
        
        /**
         * Will compare this specimen with the given <i>exemplar</i>.<br>
         * Evaluation can be retrieved with {@link EvaluatedSpecimen#getEvaluation}.
         * 
         * @param exemplar to compare this Specimen with
         * 
         * @throws IncompatibleSpecimensException when the <i>expemplar</i> can not be compared with this Specimen.
         */
        public void reevaluate(final Specimen exemplar) throws IncompatibleSpecimensException
        {
            evaluation = specimen.evaluate(exemplar);
        }
    }
}
