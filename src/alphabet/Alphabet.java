package alphabet;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents one alphabet.
 * Assigns numerical value to each letter of the alphabet.
 * Provides methods of conversion between Strings and arrays of values representing characters in Alphabet.
 * 
 * Please do note that one Alphabet is not able to convert texts written with different Object of this type.
 */
public class Alphabet
{
    /**Utilized set of characters.*/
    private String alphabet;
    /**Efficient way to find characters index in the alphabet.*/
    private Map<Character, Integer> characterMap;
    
    /**
     * Constructor specifies alphabet to be used.
     * Each letter of the provided String is representing one character of this alphabet.
     * 
     * @param alphabet to be utilized
     * 
     * @exception CharacterDuplicationException when duplicates in given <i>alphabet</i> exist
     */
    public Alphabet(final String alphabet) throws CharacterDuplicationException
    {
        this.alphabet = alphabet;
        prepareSearchStructure();
    }
    
    /**
     * @return utilized alphabet
     */
    public String getAlphabet()
    {
        return new String(alphabet);
    }
    
    /**
     * @return number of characters in this alphabet
     */
    public int getAlphabetLength()
    {
        return alphabet.length();
    }
    
    /**
     * Will convert the <i>text</i> in the form of an array of alphabet indexes to plain String.
     * 
     * Please do note that using <i>text</i> generated with different Alphabet will not always result in Exception, though the result will still be incorrect.
     * 
     * @param text to be converted
     * @return String representing the provided text
     * @exception UnknownCharacterException if an unknown character is found in the provided <i>text</i>. 
     * Index of this character will be stored as a description of the Exception.
     */
    public String convert(final int[] text) throws UnknownCharacterException
    {
        String result = "";
        
        for(int i = 0; i < text.length; ++i)
        {
            if(text[i] > alphabet.length())
            {
                throw new UnknownCharacterException(Integer.toString(text[i]));
            }
            result += alphabet.charAt(text[i]);
        }
        
        return result;
    }
    
    /**
     * Will convert the <i>text</i> in the form of plain String to an array of alphabet indexes.
     * 
     * @param text to be converted
     * 
     * @return int[] representing the provided text in the form of an array of alphabet indexes
     * 
     * @throws UnknownCharacterException if an unknown character is found in the provided <i>text</i>.
     * This character will be stored as a description of the Exception.
     */
    public int[] convert(final String text) throws UnknownCharacterException
    {
        int[] result = new int[text.length()];
        Integer characterIndex = null;
        
        for(int i = 0; i < text.length(); ++i)
        {
            characterIndex = characterMap.get(text.charAt(i));
            if(characterIndex == null)
            {
                throw new UnknownCharacterException(Character.toString(text.charAt(i)));
            }
            result[i] = characterIndex;
        }
        
        return result;
    }
    
    /**
     * Will build a structure for efficient looking up character indexes.
     * 
     * @exception CharacterDuplicationException when duplicates in given alphabet exist
     */
    private void prepareSearchStructure() throws CharacterDuplicationException
    {
        characterMap = new HashMap<Character, Integer>();
        
        for(int i = 0; i < alphabet.length(); ++i)
        {
            if(characterMap.containsKey(alphabet.charAt(i)))
            {
                throw new CharacterDuplicationException(Character.toString(alphabet.charAt(i)));
            }
            characterMap.put(alphabet.charAt(i), i);
        }
    }
    
}
