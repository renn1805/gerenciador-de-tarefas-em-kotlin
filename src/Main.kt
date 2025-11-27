import jdk.internal.org.jline.utils.ShutdownHooks
import TaskList.listTask
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyles
import javax.print.attribute.standard.JobPriority

fun greetings() {
    println("Welcome to Manager!")
}

fun goodbye() {
    println("Goodbye")
}

fun menu() {
    loop@ while (true) {
        println(
            "Welcome to Menu! \n\n" +
                    "write 1 for create new task \n" +
                    "write 2 for see all tasks\n" +
                    "write 3 for search a task\n" +
                    "write 4 for remove a task\n" +
                    "write 5 for edit a task\n" +
                    "write 6 for edit a task\n" +
                    "write 7 for exit"
        )
        when (readln().toInt()) {
            1 -> createTask()
            2 -> TaskList.showTasks()
            3 -> TaskList.searchTask()
            4 -> TaskList.removeTask()
            5 -> TaskList.editTaskState()
            6 -> TaskList.editPriorityLevelOfTask()
            7 ->  break@loop
            else -> {
                println("\ninvalid option.\n")
                continue@loop
            }
        }
    }
}

sealed class TaskState {
    object NoStarted : TaskState() {
        override fun toString(): String = "NoStarted"
    }

    object InProgress : TaskState() {
        override fun toString(): String = "InProgress"
    }

    data class Completed(val dateCompleted: String) : TaskState() {
        override fun toString(): String = "Completed in $dateCompleted"
    }
}

data class Task(
    val taskName: String,
    var taskPriorityLevel: Int,
    var currentTaskState: TaskState
) {
    fun executeTask() {
        currentTaskState = TaskState.InProgress
    }

    fun etartingTask(newState: TaskState) {
        currentTaskState = newState
    }

    fun editPriorityLevelOfTask() {
        while (true) {
            println("set the new level of the task \n1, 2, 3, 4 or 5 \nR:")
            taskPriorityLevel = readln().toInt()
            if (!(taskPriorityLevel < 1 || taskPriorityLevel > 5)) {
                println("task level has been updated")
            } else {
                break
            }
        }
    }
}

interface TaskListManager {
    fun showTasks()
    fun listTask(task: Task)
    fun searchTask()
    fun removeTask()
    fun editTaskState()
    fun editPriorityLevelOfTask()
}

object TaskList : TaskListManager {
    var list = mutableListOf<Task>()

    override fun listTask(task: Task) {
        list.add(task)
    }

    override fun showTasks() {
        if (!(list.isEmpty())) {
            println("\n\nThis is your list:")
            this.list.forEach { task ->
                println(
                    "  Name of the Task: ${task.taskName} \n  Priority of the task: ${task.taskPriorityLevel} \n  Progress of the task: ${task.currentTaskState.toString()} \n"
                )
            }
        } else {
            println("do you no have tasks!")
        }
    }

    override fun searchTask() {
        println("\n\nWrite the name of the task desired. \nR:")

        var taskNameSearch = readln().uppercase()
        list.forEach { task ->
            if (task.taskName == taskNameSearch) {
                println(
                    "\n Your Task:\n" +
                            "  Name of the Task: ${task.taskName} \n" +
                            "  Priority of the task: ${task.taskPriorityLevel} \n" +
                            "  Progress of the task: ${task.currentTaskState.toString()} \n"
                )
            }else println("task not found")
        }
    }

    override fun removeTask() {
        println("\n\nWrite the name of the task to be removed. \nR:")
        var indexTask: Int?  = null

        var taskNameRemoved = readln().uppercase()

        list.forEachIndexed { index, task ->
            if (task.taskName == taskNameRemoved) {
                indexTask = index
            }
        }

        if (indexTask != null) list.removeAt(indexTask) else println("task not found")

    }

    override fun editTaskState() {
        println("write the name of the task to be edited. \nR:")
        var taskEditedState = readln().uppercase()

        println("write 1 for conclude the task\n" +
                "write 2 for start doing the task")

        var taskEdited:Task? = null
        list.forEach {task -> if (task.taskName == taskEditedState) taskEdited = task  }

        loop@ while(true){
            when (readln().toInt()) {
                1 -> {
                    println("   write the date of the task\n R:")
                    taskEdited?.currentTaskState = TaskState.Completed(readln())
                    break@loop
                }
                2 -> {
                    taskEdited?.currentTaskState = TaskState.InProgress
                    break@loop
                }
                else -> {
                    println("\ninvalid option.\n write another option.\nR:")
                    continue@loop
                }
            }
        }
    }

    override fun editPriorityLevelOfTask() {
        println("\nWrite the name of the your task\nR:")
        var taskEdited = readln().uppercase()

        println("write from 1 to 5 tho level of the priority\nR:")
        var levelPriority = readln().toInt()

        loop@ while(true){
            if (levelPriority < 1 || levelPriority > 5) {
                println("invalid option.\n write another option.\nR:")
                levelPriority = readln().toInt()
            }else break@loop

        }

        list.forEach { task -> if (task.taskName == taskEdited)  task.taskPriorityLevel = levelPriority }
    }
}

fun createTask() {
    println("Creating a new task")

    println("report the name of the task \n R:")
    val nameTask = readln().toString().uppercase()

    println("Inform the level of priority of the task (1 - 5) \n R:")
    var priorityLevel = readln().toInt()

    while (priorityLevel > 5 || priorityLevel < 1) {

        println("priority level invalid, inform another")
        priorityLevel = readln().toInt()
    }

    println("Has this task already been started? (y/n) \n R:")
    var startedTask = readln().toString().uppercase()
    var taskState: TaskState = TaskState.NoStarted

    while (true) {

        if (startedTask == "Y") {
            taskState = TaskState.InProgress
            break
        } else if (startedTask == "N") {
            taskState = TaskState.NoStarted
            break
        } else {
            println("invalid answer, inform another: \nR:")
            startedTask = readln().toString().uppercase()
            continue
        }
    }

    val task = Task(nameTask, priorityLevel, currentTaskState = taskState)

    TaskList.listTask(task)
}

fun main() {
    greetings()
    menu()
    goodbye()
}