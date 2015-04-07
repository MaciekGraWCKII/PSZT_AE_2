package alphabet;

/**
 * Thrown if an unknown character is found.
 */
public class CharacterDuplicationException extends Exception
{
    /***/
    private static final long serialVersionUID = 1L;
    
    /**
     * @param description of the problem
     */
    public CharacterDuplicationException(final String description)
    {
        super(description);
    }
}
