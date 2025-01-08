Проектът представлява система за управление на транспортна компания, която позволява администриране на компании, служители, превозни средства, клиенти и транспортни операции. Целта е да се предостави инструмент за ефективно управление на процесите в транспортната индустрия, като се гарантира, че всички данни са структурирани, лесни за достъп и анализ.

1. Архитектура
    1.1 Entity слой (Моделът на данни):
    •	Цел: Представя базовите структури на данните, които отговарят на таблиците в базата данни.
    •	Реализация: Класовете в main.entity са анотирани с JPA анотации, които дефинират връзките и правилата за работа с базата данни.
    •	Основни характеристики:
    •	Връзка с база данни чрез Hibernate.
    
    1.2 DTO слой (Data Transfer Objects):
    •	Цел: Представя лек, оптимизиран слой за предаване на данни между различни слоеве (обикновено между DAO и UI/сервизи).
    •	Реализация: Класовете в main.dto са опростени обекти, съдържащи само данните, които са необходими за конкретна операция.

    1.3 Mapper слой:
    •	Цел: Преобразува Entity обекти в DTO обекти и обратно.
    •	Реализация: Класовете в main.mapper съдържат статични методи, които конвертират между Entity и DTO.

    1.4 DAO слой (Data Access Objects):
    •	Цел: Осигурява достъп до базата данни чрез използване на Hibernate.
    •	Реализация: Класовете в main.dao съдържат CRUD операции и сложни заявки към базата данни.

    1.5 HibernateUtil (Конфигурационен слой):
    •	Цел: Осигурява инициализация на Hibernate SessionFactory, който управлява връзките с базата данни.

2. Как работят компонентите заедно
    2.1	Получаване на данни от потребителя:
          •	Въвеждат се данни, които съответстват на DTO модела.
    2.2	Обработка на данни:
          •	DTO се преобразува в Entity чрез Mapper.
          •	DAO извършва операции върху Entity обекта в базата данни.
    2.3	Извличане на данни:
          •	DAO извлича Entity обекти от базата данни.
          •	Mapper преобразува Entity обектите обратно в DTO за използването им от потребителя.

3. Функционалности
    3.1 Управление на компании
    Създаване на компания
    •	Метод: createCompany(CompanyDTO companyDTO)
    •	Създава нова компания с име, адрес и начален капитал.

    Редактиране на компания
    •	Метод: updateCompany(CompanyDTO companyDTO, Long companyId)
    •	Актуализира данни за съществуваща компания.

    Изтриване на компания
    •	Метод: deleteCompany(Long id)
    •	Маркира компанията като изтрита (логическо изтриване).

    Преглед на всички компании
    •	Метод: getAllCompanies()
    •	Връща списък на всички активни компании.

    Справки за компания
    •	Метод: getCompaniesSortedByRevenue() - Сортира компаниите по приходи.
    •	Метод: getCompaniesSortedByName() - Сортира компаниите по име.

    Извличане на свързаните данни за компания
    •	Метод: getCompanyEmployees(Long companyId) - Извлича служителите.
    •	Метод: getCompanyClients(Long companyId) - Извлича клиентите.
    •	Метод: getCompanyVehicles(Long companyId) - Извлича превозните средства.

    Извличане на приходите за компания
    •	Метод: getTotalRevenueByCompany(Long companyId) - Извлича приходите на компанията.
    •	Метод: getTotalRevenueByCompanyInRange(Long companyId, LocalDate startDate, LocalDate endDate)  - Извлича приходите за определен период.

    Извличане на извършените транспорти за компания
    •	Метод: getTotalTransportsByCompany(Long companyId) - Извлича приходите на компанията.
    •	Метод: getTotalTransportsByCompanyInRange(Long companyId, LocalDate startDate, LocalDate endDate)  - Извлича извършените транспорти за определен период.

    Създаване на обща справка
    •	Метод: getCompanyReport(Long companyId) - Създава справка с цялостната дейност на дадена компания.

    3.2 Управление на служители
    Създаване на служител
    •	Метод: saveEmployee(EmployeeDTO employeeDTO, Long companyId)
    •	Създава нов служител и го асоциира с конкретна компания.
    
    Редактиране на служител
    •	Метод: updateEmployee(EmployeeDTO employeeDTO, Long employeeId)
    •	Актуализира данните на съществуващ служител.
    
    Изтриване на служител
    •	Метод: deleteEmployee(Long employeeId)
    •	Маркира служителя като изтрит.
    
    Преглед на всички служители
    •	Метод: getAllEmployees()
    •	Връща списък на всички активни служители.
    
    Сортиране на служители
    •	Метод: getEmployeesSortedBySalary()
    •	Сортира служителите по заплата в низходящ ред.

    3.3 Управление на клиенти
    Създаване на клиент
    •	Метод: createClient(ClientDTO clientDTO, Long companyId)
    •	Създава нов клиент и го асоциира с конкретна компания.

    Редактиране на клиент
    •	Метод: updateClient(ClientDTO clientDTO, Long clientId)
    •	Актуализира данните на съществуващ клиент.
    
    Изтриване на клиент
    •	Метод: deleteClient(Long clientId)
    •	Маркира клиента като изтрит.
    
    Преглед на всички клиенти
    •	Метод: getAllClients()
    •	Връща списък на всички активни клиенти.

    3.4. Управление на превозни средства
    Създаване на превозно средство
    •	Метод: createVehicle(VehicleDTO vehicleDTO, Long companyId)
    •	Създава ново превозно средство и го асоциира с конкретна компания.
    
    Редактиране на превозно средство
    •	Метод: updateVehicle(VehicleDTO vehicleDTO, Long vehicleId)
    •	Актуализира данните на съществуващо превозно средство.
    
    Изтриване на превозно средство
    •	Метод: deleteVehicle(Long vehicleId)
    •	Маркира превозното средство като изтрито.
    
    Преглед на всички превозни средства
    •	Метод: getAllVehicles()
    •	Връща списък на всички активни превозни средства.

    Извлича превозното средство
    •	Метод: getVehicleByRegistrationNumber()
    •	Връща превозното средство ппр подаден рег. номер
    
    3.5. Управление на транспортни операции
    Създаване на транспорт
    •	Метод: createTransport(TransportDTO transportDTO, Long companyId, Long clientId, Long employeeId, Long vehicleId)
    •	Създава нова транспортна операция.
    
    Редактиране на транспорт
    •	Метод: updateTransport(Long transportId, TransportDTO transportDTO)
    •	Актуализира данните на съществуваща транспортна операция.
    
    Изтриване на транспорт
    •	Метод: deleteTransport(Long transportId)
    •	Маркира транспортната операция като изтрита.
    
    Преглед на всички транспорти
    •	Метод: getAllTransports()
    •	Връща списък на всички активни транспортни операции.

    Филтриране на превози по дестинация
    •	Метод: getTransportsByDestination(String destination)
    •	Извлича всички активни  транспортни операции при зададена крайна дестинация.

    Справка за общи приходи от всички превози
    •	Метод: getTotalRevenue()
    •	Изчислява сумата на цените на всички платени и активни транспортни операции.
    
    Справка за общия брой на транспорти
    •	Метод: getTotalTransports()
    •	Извлича броя на всички активни транспортни операции.

    Справка за общи приходи от превозите на определен шофьор
    •	Метод: getTotalRevenueByDriver(Long driverId)
    Изчислява сумата на приходите от всички платени превози, изпълнени от конкретен шофьор, който се идентифицира чрез driverId.
    Примерна употреба: Може да използвате този метод за изчисляване на бонусите на шофьорите на база приходите от превози, които са извършили.

    3.6. Справки и отчети
    Приходи и печалби
    •	Метод: getTotalRevenueByCompany(Long companyId)
    •	Изчислява общите приходи от транспортни операции на компанията.
    •	Метод: getTotalProfitByCompany(Long companyId)
    •	Изчислява чистата печалба на компанията (приходи минус разходи за заплати).
    
    Генериране на отчет
    •	Метод: saveCompanyReportToFile(Long companyId, String directoryPath, String startDate, String endDate)
    •	Генерира текстов отчет за конкретна компания за зададен период.
    •	В отчета се включват:
    •	Общи приходи
    •	Общ брой транспортни операции
    •	Списък с превозни средства, клиенти и служители

    3.7. Валидация
    Валидация на обекти
    •	Използване на Hibernate Validator за проверка на правилата за валидация, като например:
    •	Име на компанията, което започва с главна буква.
    •	Заплатата на служителите трябва да бъде положителна.
    •	Товарът на превоза трябва да има положително тегло.
    
    Тестове за валидация
    •	Тестови класове за проверка на правилата за валидация:
    •	CompanyValidationTest
    •	EmployeeValidationTest
    •	ClientValidationTest
    •	VehicleValidationTest
    •	TransportValidationTest