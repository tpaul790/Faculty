package com.example.socialnetworkgui.utils;

import com.example.socialnetworkgui.Aplication;
import com.example.socialnetworkgui.controller.AbstractController;
import com.example.socialnetworkgui.controller.AbstractSimpleController;
import com.example.socialnetworkgui.controller.LogInController;
import com.example.socialnetworkgui.controller.SignUpController;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.service.FriendshipService;
import com.example.socialnetworkgui.service.MessageService;
import com.example.socialnetworkgui.service.UserService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

public class Utils {

    public static AbstractController setSceneOnStage (Stage stage, String resorces, String title, double width, double height) throws IOException {
        FXMLLoader loader = new FXMLLoader(Aplication.class.getResource(resorces));
        Parent root = loader.load();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.setWidth(width);
        stage.setHeight(height);
        return loader.getController();
    }

    public static void setDataForController(AbstractController controller, Stage stage, UserService userService, FriendshipService friendshipService, MessageService messageService, User user) throws IOException {
        controller.setStage(stage);
        controller.setConectedUser(user);
        controller.setServices(userService, friendshipService, messageService);
    }

    public static void setDataForSimpleController(AbstractSimpleController controller, Stage stage, UserService userService, FriendshipService friendshipService, MessageService messageService) throws IOException {
        controller.setStage(stage);
        controller.setServices(userService, friendshipService, messageService);
    }

    public static void dfsIterative(int s, int[][] matrix, boolean[] visited) {
        Stack<Integer> stack = new Stack<>();
        int n = matrix.length;
        stack.push(s);
        while (!stack.isEmpty()) {
            s = stack.pop();
            visited[s] = true;
            for (int i = 0; i < n; i++) {
                if (!visited[i] && matrix[s][i] == 1) {
                    stack.push(i);
                }
            }
        }
    }

    public static int numberOfConnectedComponents(int[][] matrix) {
        int n = matrix.length;
        boolean[] visited = new boolean[n];
        int rez = 0;
        for (int i = 0; i < n; i++) {
            if(!visited[i]) {
                dfsIterative(i, matrix, visited);
                rez++;
            }
        }
        return rez;
    }

}
