package workblue.todo.app.model;

import androidx.annotation.NonNull;

public class HelpGuild {
    String helpName;

    public HelpGuild(String helpName) {
        this.helpName = helpName;
    }

    public String getHelpName() {
        return helpName;
    }

    public void setHelpName(String helpName) {
        this.helpName = helpName;
    }

    @NonNull
    @Override
    public String toString() {
        return "HelpGuild{" + helpName + '\'' + '}';
    }
}
