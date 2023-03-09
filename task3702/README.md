<h3>В этой задаче была реализована фабрика для людей, а именно для мужчин и женщин разных возрастных категорий. </h3>

<h4>Как устроенна эта программа:</h4>

<ul style="list-style-type:circle">
    <li>Для всех типов людей создан общий интерфейс <b>Human</b>;</li>
    <li>Для мужчин и женщин созданы два пакета <b>male</b> и <b>female</b>, в которых содержатся классы KidBoy, TeenBoy, Man и KidGirl, TeenGirl, Woman, соответсвенно;</li>
    <li>В классах <b>KidBoy</b>, <b>TeenBoy</b> и <b>KidGirl</b>, <b>TeenGirl</b> создана своя публичная константа MAX_AGE с соответствующими значениями: 12, 19;</li>
    <li>Классы <b>MaleFactory</b> и <b>FemaleFactory</b> реализуют интерфейс <b>AbstractFactory</b> с методом <b>getPerson</b>, переопределяют его под нужный пол, а сам метод возращает нужный объект одного из классов, реализующих интерфейс <b>Human</b>;</li>
    <li>В классе <b>FactoryProducer</b> созданы: 
        <ul style="list-style-type:square">
            <li><b>enum HumanFactoryType</b> со значениями <b>MALE</b>, <b>FEMALE</b> </li>
        <li>метод <b>getFactory</b>, который должен принимать один параметр типа HumanFactoryType и возвращать одну из фабрик в зависимости от принятого параметра.</li>
        </ul>
    </li>
</ul>

<h4 style="align: center">Мои успехи:</h4>
<ul style="list-style-type:decimal">
    <li>Освоен паттерн Factory Method;</li>
    <li>Освоен паттерн Abstract Factory.</li>
</ul>