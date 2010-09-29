package outbox.tracking

/**
 * @author Ruslan Khmelyuk
 */
enum TrackingReferenceType {

    /**
     * Reference to resource, like image.
     */
    Resource(1),

    /**
     * Reference to redirect to specified resource
     */
    Link(2)

    final int id

    def TrackingReferenceType(id) {
        this.id = id;
    }
}
