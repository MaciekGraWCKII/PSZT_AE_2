package evolutionary_algorithm;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import randomizer.DifferentArrayLengthException;
import randomizer.NotEnoughInSrcException;
import randomizer.Randomizer;
import randomizer.SrcDstLengthDifferentException;
import randomizer.SrcsLengthDifferentException;

/**
 * Represents one individual capable of ciphering/deciphering by means of a substitution cipher.
 * It can also participate in reproduction, where the child uses a cipher key produced form keys of its parents.
 * Specimen is built from number of chromosomes, each defining one substitution.
 */
public class Specimen
{
    /**The int under index of i contains the index of a character from an alphabet for which i character of that alphabet is substituted.*/
    protected int[] chromosomes;
    protected int[] inversedChromosomes;
    protected Randomizer<Integer> randomizer = new Randomizer<Integer>();
    
    /**
     * New Specimen with given number of chromosomes, which are initialized with random values that meet the following condition:<br>
     * 0 <= chromosomeValue < <i>numberOfChromosomes</i>
     * 
     * @param numberOfChromosomes that will define this new Specimen
     * @param rand for randomization
     */
    public Specimen(final int numberOfChromosomes, final Random rand)
    {
        chromosomes = new int[numberOfChromosomes];
        Integer[] boxedInts = new Integer[numberOfChromosomes];
        
        List<Integer> numbers = new LinkedList<Integer>();
        
        for(int i = 0; i < numberOfChromosomes; ++i)
        {
            numbers.add(i);
        }
        
        try
        {
            randomizer.randomize(boxedInts, numbers, new Random());
        } 
        catch (NotEnoughInSrcException e)
        {
            throw new RuntimeException("Impossible! Arrays of length equal to numberOfChromosomes should've been passed.");
        }
        
        for(int i = 0; i < numberOfChromosomes; ++i)
        {
            chromosomes[i] = boxedInts[i];
        }
        
        createInversedChromosomes();
    } 
    
    /**
     * @param chromosomes that will define this new Specimen
     */
    protected Specimen(final int[] chromosomes)
    {
        this.chromosomes = chromosomes;
    }
    
    /**
     * Will substitute each int according to its key,<br>
     * effectively encoding given <i>message</i>.
     * 
     * @param message to be ciphered
     */
    public void encode(final int[] message) throws IndexOutOfBoundsException
    {
        substitute(message, chromosomes);
    }
    
    /**
     * Will substitute each int according to its reversed key,<br>
     * effectively decoding given <i>message</i>.
     * 
     * @param message to be deciphered
     */
    public void decode(final int[] message) throws IndexOutOfBoundsException
    {
        substitute(message, inversedChromosomes);
    }
    
    /**
     * Will create a new Specimen based on this one and <i>partner</i>.
     * 
     * @param partner to participate in the process
     * @param equalValuesStayProbability determines how likely a chromosome, which is identical in both parents, is to be passed on to the child. [0.0d ; 1.0d]
     * @param mutationChance [0.0d, 1.0d]
     * @param rand for randomization
     * 
     * @return new Specimen, which is the result of reproduction
     * 
     * @throws IncorrectProbabilityException when <i>equalValuesStayProbability</i> does not belong to [0.0d ; 1.0d]
     * @throws IncorrectMutationChanceException when <i>mutationChance</i> does not belong to [0.0d ; 1.0d]
     * @throws DifferentNumberOfChromosomesException when number of chromosomes in this Specimen and its partner is not the same
     */
    public Specimen reproduce(final Specimen partner, 
            final double equalValuesStayProbability, final double mutationChance, final Random rand)
            throws IncorrectProbabilityException, IncorrectMutationChanceException, DifferentNumberOfChromosomesException
    {
        if(chromosomes.length != partner.chromosomes.length)
        {
            throw new DifferentNumberOfChromosomesException();
        }
        int[] childsChromosomes = new int[chromosomes.length];
        Set<Integer> unused = new HashSet<Integer>();
        Set<Integer> emptyIndexes = new HashSet<Integer>();
        
        try
        {
            crossArrays(chromosomes, partner.chromosomes, childsChromosomes, equalValuesStayProbability, unused, emptyIndexes, rand);
        }
        catch (SrcsLengthDifferentException | SrcDstLengthDifferentException e)
        {
            throw new RuntimeException(
                    "Impossible! Number of chromosomes in parents has been checked and childs number of chromosomes has been set to this value.");
        }
        
        //Since both parents hold same values in their chromosomes and their child inherits those values and unused holds no duplicates,
        //there should be equal amount of leftover values and idexes that have yet to be filled.
        assert(unused.size() == emptyIndexes.size());
        
        fillChromosomesWithLeftovers(childsChromosomes, unused, emptyIndexes, rand);
        tryForMutation(childsChromosomes, mutationChance, rand);
        
        return new Specimen(childsChromosomes);
    }
    
    /**
     * Will compare this specimen with the given <i>exemplar</i>.<br>
     * Returns value which can be interpreted as percentage of sameness.<br>
     * 
     * @param exemplar to be compared with this Specimen
     * 
     * @return percentage of sameness [0.0d ; 1.0d]
     * 
     * @exception IncompatibleSpecimensException when the <i>expemplar</i> can not be compared with this Specimen.
     */
    public double evaluate(final Specimen exemplar) throws IncompatibleSpecimensException
    {
        try
        {
            return randomizer.compare(this.chromosomes, exemplar.chromosomes);
        } 
        catch (DifferentArrayLengthException e)
        {
            throw new IncompatibleSpecimensException();
        }
    }
    
    /**
     * Will compare values from <i>src1</i>, <i>src2</i> and:<br>
     * -if they are equal, it will insert this value into <i>dst</i> with given <i>probability</i><br>
     * -in other cases, it will insert read values into <i>unused</i> and put the index in <i>emptyIndexes</i><br>
     * Please, do note that <i>unused</i> is a {@link Set}.
     * All values from src1 should occur in src2 lest unintended results may occur.
     *
     * @param src1 to compare with <i>src2</i>
     * @param src2 to compare with <i>src1</i>
     * @param dst to store equal values in
     * @param probability with which equal values are passed on to <i>dst</i> untouched; [0.0d - 1.0d]
     * @param unused to store unequal values from <i>src1</i> and <i>src2</i>
     * @param emptyIndexes of <i>dst</i>, where no value was inserted
     * @param rand for randomization
     * 
     * @exception SrcsLengthDifferentException if <i>src1</i>.length != <i>src2</i>.length
     * @exception SrcDstLengthDifferentException if size of <i>dst</i> is not equal to <i>src</i> size 
     * @exception IncorrectProbabilityException if probability is not in range [0.0d - 1.0d]
     */
    protected void crossArrays(final int src1[], final int src2[], 
            final int dst[], final double probability, 
            final Set<Integer> unused, final Set<Integer> emptyIndexes, final Random rand)
            throws SrcsLengthDifferentException, SrcDstLengthDifferentException, IncorrectProbabilityException
    {
        if(src1.length != src2.length)
        {
            throw new SrcsLengthDifferentException();
        }
        if(src1.length != dst.length)
        {
            throw new SrcDstLengthDifferentException();
        }
        if(probability < 0.0d && probability > 1.0d)
        {
            throw new IncorrectProbabilityException();
        }
        for(int i = 0; i < src1.length; ++i)
        {
            if(src1[i] == src2[i] && rand.nextDouble() <= probability)
            {
                dst[i] = src1[i];
            }
            else
            {
                emptyIndexes.add(i);
                unused.add(src1[i]);
                unused.add(src2[i]);
            }
        }
    }
    
    /**
     * @param chromosomes with some indexes still empty
     * @param unused values to fill with
     * @param emptyIndexes to be filled
     * @param rand for randomization
     */
    protected void fillChromosomesWithLeftovers(final int[] chromosomes, final Set<Integer> unused, final Set<Integer> emptyIndexes, final Random rand)
    {
        //Here we are certain that unused contains Integers.
        Integer[] unusedValues = (Integer[]) unused.toArray();
        //Here we are certain that emptyIndexes contains Integers.
        Integer[] unusedIndexes = (Integer[]) emptyIndexes.toArray();
        
        randomizer.randomize(unusedValues, rand);
        
        for(int i = 0; i < unusedValues.length; ++i)
        {
           chromosomes[unusedIndexes[i]] = unusedValues[i];
        }
    
    }
    
    /**
     * @param chromosomes to be mutated
     * @param rand for randomization
     */
    protected void tryForMutation(final int[] chromosomes, final double mutationChance, final Random rand)
    {
        if(chromosomes.length <= 1)
        {
            return;
        }
        if(rand.nextDouble() < mutationChance)
        {
            int first = rand.nextInt(chromosomes.length);
            int second = rand.nextInt(chromosomes.length);
            int tmp = 0;
            
            while(second == first)
            {
                second = rand.nextInt(chromosomes.length);
            }
            
            tmp = chromosomes[first];
            chromosomes[first] = chromosomes[second];
            chromosomes[second] = tmp;
        }
    }
    
    /**
     * For deciphering purposes.
     */
    protected void createInversedChromosomes()
    {
        inversedChromosomes = new int[chromosomes.length];
        
        for(int i = 0; i < chromosomes.length; ++i)
        {
            inversedChromosomes[chromosomes[i]] = i;
        }
        
    }

    /**
     * Will substitute each int according to the key.
     * 
     * @param message
     * @param key
     */
    protected void substitute(final int[] message, final int[] key)
    {
        for(int i = 0; i < message.length; ++i)
        {
            message[i] = key[message[i]];
        }
    }
}
