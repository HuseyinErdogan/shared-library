package hus.erd

abstract class Command {

    /** Product that the command run for if exists and set in command **/
    protected String product = ''

    /** Chronicle constants and times **/
    public static final String CHRONICLES = 'chronicles'
    private static final String PLATEAU = 'plateau'
    protected Date startTime
    protected Date endTime

    /** These fields can be used to update merge request description if merge request number exists **/
    protected Exception commandException
    protected String failLog = ''
    protected String statusLog = ''

    /** Stores name of branches if a branch restriction exists **/
    protected Set<String> allowedBranchNames
    protected Boolean branchRestricted

    /** These fields can be used to discriminate the same commands used in pipeline **/
    protected String description = ''
    public String comment = ''

    /** Command metadata */
    protected Map<String, Object> metadata

    /** Generated command closure body which will run during pipeline execution **/
    protected Closure body

    /** Build command that prepares the closure to run */
    abstract Command build()


    Command() {
        metadata = [:]
        branchRestricted = false
        allowedBranchNames = []
        startTime = new Date()
        endTime = new Date()
        comment = ''
    }

}
