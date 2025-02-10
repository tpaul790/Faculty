namespace Sem10;

public interface Factory
{
    AbstractContainer createContainer(Strategy strategy);
}