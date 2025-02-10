package Factory;

import Domain.Container;

public interface Factory {
    Container createContainer(Strategy strategy);
}
