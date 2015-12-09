package uk.ac.leeds.atcg_gdl.courseworktracker;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a single coursework item.
 */
public class Coursework implements Serializable {
    private String moduleName;
    private String courseworkName;
    private Date deadline;
    private int weight;
    private String notes;
    private boolean completed;

    /**
     * Creates a new Coursework instance
     *
     * @param moduleName     The name of the module that the coursework is part of
     * @param courseworkName The name of the coursework
     * @param deadline       The coursework's deadline date
     * @param weight         The weight, as a percentage from 1 to 100
     * @param notes          The users notes
     * @param completed      True if the coursework has been marked as completed
     */
    public Coursework(String moduleName, String courseworkName, Date deadline, int weight, String notes, boolean completed) {
        this.moduleName = moduleName;
        this.courseworkName = courseworkName;
        this.deadline = deadline;
        this.weight = weight;
        this.notes = notes;
        this.completed = completed;
    }

    /**
     * @return The name of the module that the coursework is part of
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * @return The name of the coursework
     */
    public String getCourseworkName() {
        return courseworkName;
    }

    /**
     * @return The coursework's deadline date
     */
    public Date getDeadline() {
        return deadline;
    }

    /**
     * @return The deadline date as a formatted string
     */
    public String getFormattedDeadline() {
        return new SimpleDateFormat("EEE dd-MM-yyyy").format(deadline);
    }

    /**
     * @return The weight, as a percentage from 1 to 100
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @return The users notes for the coursework
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @return True if the coursework has been marked as completed
     */
    public boolean getCompleted() {
        return completed;
    }
}