package alphabet;

/**
 * Provides the default alphabet: "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz,. "
 */
public class DefaultAlphabet extends Alphabet
{
    /**
     * @throws CharacterDuplicationException 
     */
    private DefaultAlphabet() throws CharacterDuplicationException
    {
            super("AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz,. ");
    }
    
}
