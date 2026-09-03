package bott.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * A single chat bubble in the conversation: one speaker's message shown as
 * a wrapped-text label in a rounded, coloured box. Messages the user typed
 * are aligned to the right; Bott's replies to the left.
 */
public class DialogBox extends HBox {

    /** Colours distinguishing the two speakers. */
    private static final Color COLOR_USER = Color.web("#d0e6ff");
    private static final Color COLOR_BOTT = Color.web("#ededed");

    private static final CornerRadii CORNER_RADII = new CornerRadii(10);
    private static final Insets BUBBLE_PADDING = new Insets(8);
    private static final double SPACING = 8;

    /** Caps the bubble width so long replies wrap instead of stretching the window. */
    private static final double MAX_BUBBLE_WIDTH = 280;

    private DialogBox(String text, Color color, Pos alignment) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMaxWidth(MAX_BUBBLE_WIDTH);
        label.setPadding(BUBBLE_PADDING);
        label.setBackground(new Background(new BackgroundFill(color, CORNER_RADII, Insets.EMPTY)));

        setSpacing(SPACING);
        setPadding(BUBBLE_PADDING);
        setAlignment(alignment);
        getChildren().add(label);
    }

    /**
     * Returns a right-aligned bubble for a message the user typed.
     *
     * @param text Text the user entered.
     */
    public static DialogBox getUserDialog(String text) {
        return new DialogBox(text, COLOR_USER, Pos.TOP_RIGHT);
    }

    /**
     * Returns a left-aligned bubble for one of Bott's replies.
     *
     * @param text Text of Bott's reply.
     */
    public static DialogBox getBottDialog(String text) {
        return new DialogBox(text, COLOR_BOTT, Pos.TOP_LEFT);
    }
}
