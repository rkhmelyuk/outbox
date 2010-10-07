package outbox.report.metadata

/**
 * The information about extractor parameter.
 *
 * @author Ruslan Khmelyuk
 * @since  2010-10-07
 */
class Parameter {

    String name
    ParameterType type
    boolean required

    public String toString ( ) {
        "Parameter[name=$name, type=$type, required=$required]"
    }

}
