package randomizer;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import evolutionary_algorithm.IncorrectProbabilityException;


public class RandomizerTest
{

    @Test
    public void testThrowing()
    {
        Integer[] array = new Integer[5];
        List<Integer> list = new ArrayList<Integer>();
        Random rand = new Random();
        try
        {
            new Randomizer<Integer>().randomize(array, list, rand);
        }
        catch(NotEnoughInSrcException e)
        {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testCorrectNumberOfLeftovers()
    {
        Integer[] array = new Integer[5];
        List<Integer> list = new ArrayList<Integer>();
        Random rand = new Random();
        for(int i = 0; i < 10; ++i)
        {
            list.add(i);
        }
        try
        {
            new Randomizer<Integer>().randomize(array, list, rand);
        }
        catch(NotEnoughInSrcException e)
        {
            assertNull(e);
        }
        assertEquals(5, list.size());
        try
        {
            new Randomizer<Integer>().randomize(array, list, rand);
        }
        catch(NotEnoughInSrcException e)
        {
            assertNull(e);
        }
        assertEquals(0, list.size());
    }
    
    @Test
    public void testAllIndexesOccupiedWithDifferentSrcValues()
    {
        Integer[] array = new Integer[5];
        List<Integer> list = new ArrayList<Integer>();
        Random rand = new Random();
        for(int i = 0; i < 5; ++i)
        {
            list.add(i);
        }
        try
        {
            new Randomizer<Integer>().randomize(array, list, rand);
        }
        catch(NotEnoughInSrcException e)
        {
            assertNull(e);
        }
        boolean[] valArray = new boolean[5];
        boolean allIndexesOccupiedWithDifferentSrcValues = true;
        for(Integer i : array)
        {
            valArray[i] = true;
        }
        for(boolean b : valArray)
        {
            if(b != true)
            {
                allIndexesOccupiedWithDifferentSrcValues = false;
            }
        }
        assertTrue(allIndexesOccupiedWithDifferentSrcValues);
    }
    
    /*@Test
    public void testCrossArraysIncorrectArgs()
    {
        int[] src1 = new int[5];
        int[] src2 = new int[5];
        int[] srcWrongLength = new int[6];
        int[] dst = new int[5];
        int[] dstWrongLength = new int[6];
        Set<Integer> unused = new TreeSet<Integer>();
        Set<Integer> emptyIndexes = new TreeSet<Integer>();
        try
        {
            new Randomizer<Integer>().crossArrays(src1, srcWrongLength, dst, 0.5d, unused, emptyIndexes);
        }
        catch(SrcsLengthDifferentException e)
        {
            assertNotNull(e);
        }
        catch(SrcDstLengthDifferentException e)
        {
            assertNull(e);
        }
        catch(IncorrectProbabilityException e)
        {
            assertNull(e);
        }
        
        try
        {
            new Randomizer<Integer>().crossArrays(src1, src2, dstWrongLength, 0.5d, unused, emptyIndexes);
        }
        catch(SrcsLengthDifferentException e)
        {
            assertNull(e);
        }
        catch(SrcDstLengthDifferentException e)
        {
            assertNotNull(e);
        }
        catch(IncorrectProbabilityException e)
        {
            assertNull(e);
        }
        
        try
        {
            new Randomizer<Integer>().crossArrays(src1, src2, dst, -0.5d, unused, emptyIndexes);
        }
        catch(SrcsLengthDifferentException e)
        {
            assertNull(e);
        }
        catch(SrcDstLengthDifferentException e)
        {
            assertNull(e);
        }
        catch(IncorrectProbabilityException e)
        {
            assertNotNull(e);
        }
        
        try
        {
            new Randomizer<Integer>().crossArrays(src1, src2, dst, 1.5d, unused, emptyIndexes);
        }
        catch(SrcsLengthDifferentException e)
        {
            assertNull(e);
        }
        catch(SrcDstLengthDifferentException e)
        {
            assertNull(e);
        }
        catch(IncorrectProbabilityException e)
        {
            assertNotNull(e);
        }
    }

    @Test
    public void testCrossArraysSetsFilledCorrectly()
    {
        int[] src1 = new int[5];
        int[] src2 = new int[5];
        int[] dst = new int[5];
        Set<Integer> unused = new TreeSet<Integer>();
        Set<Integer> emptyIndexes = new TreeSet<Integer>();
        
        for(int i = 0; i < 5; ++i)
        {
            src1[i] = i;
            src2[4 - i] = i;
        }
        try
        {
            new Randomizer<Integer>().crossArrays(src1, src2, dst, 0.5d, unused, emptyIndexes);
        }
        catch(SrcsLengthDifferentException e)
        {
            assertNull(e);
        }
        catch(SrcDstLengthDifferentException e)
        {
            assertNull(e);
        }
        catch(IncorrectProbabilityException e)
        {
            assertNull(e);
        }
        boolean allNumbersInUnused = true;
        boolean allIndexesInEmptyIndexes = true;
        for(int i = 0; i < 5; ++i)
        {
            if(!unused.contains(i))
            {
                allNumbersInUnused = false;
            }
            if(!emptyIndexes.contains(i))
            {
                allIndexesInEmptyIndexes = false;
            }
        }
        assertTrue(allNumbersInUnused);
        assertTrue(allIndexesInEmptyIndexes);
    }
    
    @Test
    public void testCrossArraysProbability1()
    {
        int[] src1 = new int[5];
        int[] src2 = new int[5];
        int[] dst = new int[5];
        Set<Integer> unused = new TreeSet<Integer>();
        Set<Integer> emptyIndexes = new TreeSet<Integer>();
        
        for(int i = 0; i < 5; ++i)
        {
            src1[i] = i;
            src2[i] = i;
        }
        try
        {
            new Randomizer<Integer>().crossArrays(src1, src2, dst, 1.0d, unused, emptyIndexes);
        }
        catch(SrcsLengthDifferentException e)
        {
            assertNull(e);
        }
        catch(SrcDstLengthDifferentException e)
        {
            assertNull(e);
        }
        catch(IncorrectProbabilityException e)
        {
            assertNull(e);
        }
        boolean allValuesInDst = true;
        for(int i = 0; i < 5; ++i)
        {
            if(dst[i] != i)
            {
                allValuesInDst = false;
            }
        }
        assertTrue(allValuesInDst);
        assertTrue(unused.isEmpty());
        assertTrue(emptyIndexes.isEmpty());
    }*/
    
    @Test
    public void testCompareHalfSame()
    {
        int[] a = {5,10,15,20};
        int[] b = {5,10,20,25};
        try
        {
            assertTrue(new Randomizer<Integer>().compare(a,b) == 0.5d);
        } 
        catch (DifferentArrayLengthException e)
        {
            assertNull(e);
        }
    }
    
    @Test
    public void testCompareEverythingSame()
    {
        int[] a = {5, 10, 15, 20, 25};
        try
        {
            assertTrue(new Randomizer<Integer>().compare(a, a) == 1.0d);
        } 
        catch (DifferentArrayLengthException e)
        {
            assertNull(e);
        }
    }
}
