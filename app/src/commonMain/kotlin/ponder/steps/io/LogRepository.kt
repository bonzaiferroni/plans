package ponder.steps.io

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import ponder.steps.model.data.LogEntry
import ponder.steps.model.data.StepOutcome

interface LogRepository {
    /**
     * Get a log entry by its ID
     * @param logEntryId The ID of the log entry to retrieve
     * @return The log entry, or null if not found
     */
    suspend fun getLogEntry(logEntryId: String): LogEntry?
    
    /**
     * Get a flow of a log entry by its ID
     * @param logEntryId The ID of the log entry to retrieve
     * @return Flow of the log entry
     */
    fun flowLogEntry(logEntryId: String): Flow<LogEntry>
    
    /**
     * Get all log entries for a step
     * @param stepId The ID of the step
     * @return List of log entries for the step
     */
    suspend fun getLogEntriesByStepId(stepId: String): List<LogEntry>
    
    /**
     * Get all log entries for a trek
     * @param trekId The ID of the trek
     * @return List of log entries for the trek
     */
    suspend fun getLogEntriesByTrekId(trekId: String): List<LogEntry>
    
    /**
     * Get a flow of all log entries for a trek
     * @param trekId The ID of the trek
     * @return Flow of list of log entries for the trek
     */
    fun flowLogEntriesByTrekId(trekId: String): Flow<List<LogEntry>>
    
    /**
     * Get all log entries with a specific outcome
     * @param outcome The outcome to filter by
     * @return List of log entries with the specified outcome
     */
    suspend fun getLogEntriesByOutcome(outcome: StepOutcome): List<LogEntry>
    
    /**
     * Get all log entries in a time range
     * @param startTime The start of the time range
     * @param endTime The end of the time range
     * @return List of log entries in the time range
     */
    suspend fun getLogEntriesInTimeRange(startTime: Instant, endTime: Instant): List<LogEntry>
}