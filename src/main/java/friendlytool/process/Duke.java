package friendlytool.process;

import friendlytool.command.CommandTypes;
import friendlytool.command.Parser;

import java.util.Scanner;

/**
 * Main class for this app.
 */
public class Duke {
    private boolean isActive;
    private TaskList tasks;

    /**
     * Constructs Duke
     */
    public Duke() {
        this.isActive = true;
        this.tasks = new TaskList();
    }

    public static void main(String[] args) {
        Duke ft = new Duke();
        ft.init();
    }

    /**
     * Starts and load saved tasks.
     */
    public void init() {
        UI.initMsg();
        try {
            Storage.loadTask(tasks);
        } catch (FTException e) {
            System.out.println(e.getMessage());
        }
        Scanner sc = new Scanner(System.in);
        while (this.isActive) {
            String input = sc.nextLine();
            try {
                nextAction(input);
            } catch (FTException e) {
                System.out.println(e.getMessage());
            }
        }
        UI.byeMsg();
    }

    /**
     * Decides the next action based on the input.
     *
     * @param input user input.
     * @throws FTException
     */
    private void nextAction(String input) throws FTException {
        if (input.isEmpty()) {
            throw new FTException("Error: Please Type Command");
        }
        try {
            CommandTypes command = Parser.parseType(input);
            switch (command) {
            case BYE:
                this.isActive = false;
                break;
            case LIST:
                UI.showList(tasks);
                break;
            case MARK:
                tasks.mark(input);
                Storage.updateTask(tasks);
                break;
            case UNMARK:
                tasks.unmark(input);
                Storage.updateTask(tasks);
                break;
            case TODO:
                tasks.addTask(input, CommandTypes.TODO);
                Storage.updateTask(tasks);
                break;
            case DEADLINE:
                tasks.addTask(input, CommandTypes.DEADLINE);
                Storage.updateTask(tasks);
                break;
            case EVENT:
                tasks.addTask(input, CommandTypes.EVENT);
                Storage.updateTask(tasks);
                break;
            case DELETE:
                tasks.deleteTask(input);
                Storage.updateTask(tasks);
                break;
            default:
                throw new FTException("Unknown Command: Please use a correct command");
            }
        } catch (
                IllegalArgumentException e) {
            throw new FTException("Unknown Command: Please use a correct command");
        }
    }
}

