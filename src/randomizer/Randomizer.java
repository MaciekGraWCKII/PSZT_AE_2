package randomizer;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import evolutionary_algorithm.IncorrectProbabilityException;


/**
 * Helper for randomizing elements in efficient way.
 */
public class Randomizer<T>
{
    /**
     * Will insert items from <i>src</i> to <i>dst</i> in random order.
     * Will remove said elements from the <i>src</i>.
     * Will leave superflous elements in <i>src</i>.
     * 
     * @param dst to store items in
     * @param src to pick items from
     * @param rand to randomize
     * 
     * @exception NotEnoughInSrcException if there is less in <i>src</i> than necessary
     */
    public void randomize(final T[] dst, final List<T> src, final Random rand) throws NotEnoughInSrcException
    {
        if(dst.length > src.size())
        {
            throw new NotEnoughInSrcException();
        }
        for(int i = 0; i < dst.length; ++i)
        {
            dst[i] = src.remove(rand.nextInt(src.size()));
        }
    }
    
    /**
     * Will permutate elements in <i>array</i>.
     * 
     * @param array
     * @param rand
     */
    public void randomize(final T[] array, final Random rand)
    {
        T tmp = null;
        int index = 0;
        
        for(int i = 0; i < array.length; ++i)
        {
            index = rand.nextInt(array.length);
            tmp = array[i];
            array[i] = array[index];
            array[index] = tmp;
        }
    }

    public double compare(final int[] a, final int[] b) throws DifferentArrayLengthException
    {
        int numberOfEquals = 0;
        for(int i = 0; i < a.length; ++i)
        {
            if(a[i] == b[i])
            {
                ++numberOfEquals;
            }
        }
        return ((double)numberOfEquals) / a.length;
    }
}
