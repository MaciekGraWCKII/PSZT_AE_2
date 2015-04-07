package alphabet;

/**
 * Thrown if an unknown character is found.
 */
public class UnknownCharacterException extends Exception
{
    /***/
    private static final long serialVersionUID = 1L;
    
    /**
     * @param description of the problem
     */
    public UnknownCharacterException(final String description)
    {
        super(description);
    }
}
