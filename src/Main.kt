import jdk.internal.org.jline.utils.ShutdownHooks
import TaskList.listTask
import javax.print.attribute.standard.JobPriority

fun greetings(){println("Welcome to Manager!")}
fun goodbye(){println("Goodbye")}

fun menu(){
    loop@ while(true) {
        println("Welcome to Menu! \n\n" +
                "write 1 for create new task \n" +
                "write 2 for see all tasks\n" +
                "write 3 for exit"
        )
        when (readln().toInt()){
            1 -> createTask()
            2 -> TaskList.showTasks()
            3 -> loop@ break
        }
    }
}

sealed class TaskState {
    object NoStarted: TaskState(){
        override fun toString(): String = "NoStarted"
    }
    object InProgress: TaskState(){
        override fun toString(): String = "InProgress"
    }

    data class Completed(val dateCompleted: String): TaskState(){
        override fun toString(): String = "Completed in $dateCompleted"
    }
}

data class Task (
    val taskName: String,
    var taskPriorityLevel: Int,
    var currentTaskState: TaskState
){
    fun executeTask(){
        currentTaskState = TaskState.InProgress
    }

    fun etartingTask(newState: TaskState) {
        currentTaskState = newState
    }

    fun editPriorityLevelOfTask(){
        while(true){
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
interface TaskListManager{
    fun showTasks()
    fun listTasks(task: Task)
    fun searchTask(taskName: String)
}
object TaskList{
    var list = mutableListOf<Task>()

    fun listTask (task: Task){
        list.add(task)
    }

    fun showTasks(){
        if (!(list.isEmpty())){
            println("\n\nThis is your list:")
            this.list.forEach {
                TASK -> println(
                    "  Name of the Task: ${TASK.taskName} \n  Priority of the task: ${TASK.taskPriorityLevel} \n  Progress of the task: ${TASK.currentTaskState.toString()} \n"
                )
            }
        }else{
            println("do you no have tasks!")
        }
    }
}

fun createTask(){
    println("Creating a new task")

    println("report the name of the task \n R:")
    val nameTask = readln().toString().uppercase()

    println("Inform the level of priority of the task (1 - 5) \n R:")
    var priorityLevel = readln().toInt()

    while(priorityLevel > 5 || priorityLevel < 1){

        println("priority level invalid, inform another")
        priorityLevel = readln().toInt()
    }

    println("Has this task already been started? (y/n) \n R:")
    var startedTask = readln().toString().uppercase()
    var taskState: TaskState = TaskState.NoStarted

    while(true){

        if (startedTask == "Y"){
            taskState = TaskState.InProgress
            break
        } else if(startedTask == "N"){
            taskState = TaskState.NoStarted
            break
        } else {
            println("invalid answer, inform another: \nR:")
            startedTask = readln().toString().uppercase()
            continue
        }
    }

    val task = Task(nameTask,priorityLevel, currentTaskState = taskState)

    TaskList.listTask(task)
}
fun main (){
    greetings()
    menu()
    goodbye()
}