package com.ddangkhoa.trafficfx.ui;

import com.ddangkhoa.trafficfx.MainApp;
import com.ddangkhoa.trafficfx.model.AppUser;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DashboardView {
    private final Stage stage;
    private final AppUser user;
    private final BorderPane root = new BorderPane();
    private final VBox menuBox = new VBox(10);
    private final StackPane content = new StackPane();
    private Button activeMenuButton;
    private final Map<String, Button> menuButtons = new HashMap<>();

    public DashboardView(Stage stage, AppUser user) {
        this.stage = stage;
        this.user = user;
    }

    public Parent getView() {
        root.getStyleClass().add("dashboard-root");
        root.setLeft(buildSidebar());
        root.setTop(buildTopbar());
        root.setCenter(content);
        showHome();
        return root;
    }

    private VBox buildSidebar() {
        VBox sidebar = new VBox(18);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPadding(new Insets(26, 20, 26, 20));
        sidebar.setPrefWidth(280);

        Label logo = new Label("TP.HCM\nTRAFFIC CENTER");
        logo.getStyleClass().add("sidebar-logo");

        Label role = new Label(user.getRole().getDisplayName());
        role.getStyleClass().add("role-pill");

        Label menuTitle = new Label("CHỨC NĂNG");
        menuTitle.getStyleClass().add("menu-title");

        menuBox.getChildren().clear();
        menuButtons.clear();

        for (String feature : RoleFeatures.featuresFor(user.getRole())) {
            Button b = new Button(feature);
            b.getStyleClass().add("menu-button");
            b.setMaxWidth(Double.MAX_VALUE);
            b.setOnAction(e -> {
                setActiveMenuButton(b);
                showManagement(feature);
            });
            menuButtons.put(feature, b);
            menuBox.getChildren().add(b);
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button logout = new Button("Đăng xuất");
        logout.getStyleClass().add("logout-button");
        logout.setMaxWidth(Double.MAX_VALUE);
        logout.setOnAction(e -> {
            LoginView loginView = new LoginView(stage);
            Scene scene = new Scene(loginView.getView(), MainApp.WIDTH, MainApp.HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
            stage.setScene(scene);
        });

        sidebar.getChildren().addAll(logo, role, menuTitle, menuBox, spacer, logout);
        return sidebar;
    }

    private HBox buildTopbar() {
        HBox topbar = new HBox(16);
        topbar.getStyleClass().add("topbar");
        topbar.setPadding(new Insets(18, 28, 18, 28));
        topbar.setAlignment(Pos.CENTER_LEFT);

        VBox userBox = new VBox(2);
        Label hello = new Label("Xin chào, " + user.getFullName());
        hello.getStyleClass().add("topbar-title");
        Label detail = new Label(user.getRole().getDisplayName() + " • " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        detail.getStyleClass().add("topbar-subtitle");
        userBox.getChildren().addAll(hello, detail);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label badge = new Label("Dữ liệu mô phỏng");
        badge.getStyleClass().add("status-badge");
        topbar.getChildren().addAll(userBox, spacer, badge);
        return topbar;
    }

    private void showHome() {
        clearActiveMenuButton();

        VBox home = new VBox(24);
        home.setPadding(new Insets(32));

        Label title = new Label("Bảng điều khiển " + user.getRole().getDisplayName());
        title.getStyleClass().add("page-title");

        Label desc = new Label(RoleFeatures.roleDescription(user.getRole()));
        desc.getStyleClass().add("page-desc");
        desc.setWrapText(true);

        HBox cards = new HBox(18);
        cards.getChildren().addAll(
                metricCard("128.540", "Tổng phương tiện", "+12.5%"),
                metricCard("32 km/h", "Tốc độ trung bình", "-5%"),
                metricCard("42/100", "Chỉ số ùn tắc", "-8%"),
                metricCard(String.valueOf(RoleFeatures.featuresFor(user.getRole()).size()), "Chức năng khả dụng", "Role")
        );

        FlowPane featureGrid = new FlowPane();
        featureGrid.setHgap(16);
        featureGrid.setVgap(16);

        for (String feature : RoleFeatures.featuresFor(user.getRole())) {
            VBox f = new VBox(8);
            f.getStyleClass().add("feature-card");
            f.setPrefWidth(240);

            Label ft = new Label(feature);
            ft.getStyleClass().add("feature-title");

            Label fd = new Label("Mở màn hình thao tác dữ liệu tương ứng với Use Case: " + feature + ".");
            fd.getStyleClass().add("feature-desc");
            fd.setWrapText(true);

            Button open = new Button("Mở chức năng");
            open.getStyleClass().add("small-button");
            open.setOnAction(e -> {
                Button relatedButton = menuButtons.get(feature);
                if (relatedButton != null) {
                    setActiveMenuButton(relatedButton);
                }
                showManagement(feature);
            });

            f.getChildren().addAll(ft, fd, open);
            featureGrid.getChildren().add(f);
        }

        home.getChildren().addAll(title, desc, cards, featureGrid);
        content.getChildren().setAll(home);
    }

    private VBox metricCard(String value, String title, String change) {
        VBox card = new VBox(8);
        card.getStyleClass().add("metric-card");
        card.setPrefWidth(190);
        Label v = new Label(value);
        v.getStyleClass().add("metric-value");
        Label t = new Label(title);
        t.getStyleClass().add("metric-title");
        Label c = new Label(change);
        c.getStyleClass().add("metric-change");
        card.getChildren().addAll(v, t, c);
        return card;
    }

    private void showManagement(String title) {
        ManagementPage page = new ManagementPage(title, user);
        content.getChildren().setAll(page.getView());
    }

    private void setActiveMenuButton(Button selectedButton) {
        if (activeMenuButton != null) {
            activeMenuButton.getStyleClass().remove("menu-button-active");
            if (!activeMenuButton.getStyleClass().contains("menu-button")) {
                activeMenuButton.getStyleClass().add("menu-button");
            }
        }
        selectedButton.getStyleClass().remove("menu-button");
        if (!selectedButton.getStyleClass().contains("menu-button-active")) {
            selectedButton.getStyleClass().add("menu-button-active");
        }
        activeMenuButton = selectedButton;
    }

    private void clearActiveMenuButton() {
        if (activeMenuButton != null) {
            activeMenuButton.getStyleClass().remove("menu-button-active");
            if (!activeMenuButton.getStyleClass().contains("menu-button")) {
                activeMenuButton.getStyleClass().add("menu-button");
            }
            activeMenuButton = null;
        }
    }
}
