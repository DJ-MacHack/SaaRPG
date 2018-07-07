package de.unisaarland.sopra.gui;

import de.unisaarland.sopra.utility.GameVector;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Team 14 on 25.09.2016.
 */
public class InputListener implements KeyListener, MouseListener {

    private GuiFrame frame;

    public InputListener(GuiFrame frame) {
        this.frame = frame;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        GameVector viewPos = frame.getViewPosition();

        if ((key == KeyEvent.VK_W) || (key == KeyEvent.VK_UP)) {
            viewPos = viewPos.add(new GameVector(0, -1));
        } else if ((key == KeyEvent.VK_A) || (key == KeyEvent.VK_LEFT)) {
            viewPos = viewPos.add(new GameVector(-1, 0));
        } else if ((key == KeyEvent.VK_S) || (key == KeyEvent.VK_DOWN)) {
            viewPos = viewPos.add(new GameVector(0, 1));
        } else if ((key == KeyEvent.VK_D) || (key == KeyEvent.VK_RIGHT)) {
            viewPos = viewPos.add(new GameVector(1, 0));
        }
        frame.setViewPosition(viewPos);

        if (key == KeyEvent.VK_SPACE) {
            frame.setSelectedFieldToCurrentPlayer();
            frame.setViewPositionToCurrentPlayer();
        }

        frame.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        double selectedX = (mouseX * Math.sqrt(3.0) / 3.0 - mouseY / 3.0) / GuiFrame.SIZE;
        double selectedY = mouseY * 2.0 / 3.0 / GuiFrame.SIZE;

        GameVector viewPos = frame.getViewPosition();
        double off = viewPos.getY() / 2.0 + 0.5;
        selectedX += viewPos.getX() + 1 - off;
        selectedY += viewPos.getY();

        GameVector pos = new GameVector((int) Math.floor(selectedX), (int) Math.floor(selectedY));
        frame.setSelectedField(pos);
        frame.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        return;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        return;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        return;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        return;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        return;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        return;
    }
}
