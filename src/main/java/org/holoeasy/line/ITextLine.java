package org.holoeasy.line;

import org.bukkit.entity.Player;

public interface ITextLine extends ILine<String> {
    boolean getClickable();

    TextLine getTextLine();

    Object[] getArgs();

    String parse(Player player);

    void onClick(ClickEvent clickEvent);
}