package org.cyberiantiger.slud;

import dagger.Subcomponent;
import org.cyberiantiger.slud.net.Connection;

@ConnectionScope
@Subcomponent(modules = ConnectionModule.class)
public interface ConnectionComponent {

    public Connection getConnection();
}
