package evolutionary_algorithm;

public interface Statistics
{
    public abstract void newGeneration(final String messageTranslatedByBestSpecimen, 
            final double bestEvaluation, final double meanEvaluation, final double worstEvaluation);
}
