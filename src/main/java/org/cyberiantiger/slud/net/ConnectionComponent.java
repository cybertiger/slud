package org.cyberiantiger.slud.net;

import dagger.Subcomponent;

@ConnectionScope
@Subcomponent(modules = ConnectionModule.class)
public interface ConnectionComponent {

    public Connection getConnection();
}
