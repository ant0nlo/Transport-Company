# Система за управление на транспортна компания

Проектът представлява система за управление на транспортна компания, която позволява администриране на компании, служители, превозни средства, клиенти и транспортни операции. Целта е да се предостави инструмент за ефективно управление на процесите в транспортната индустрия, като се гарантира, че всички данни са структурирани, лесни за достъп и анализ.

---

## 1. Архитектура

### 1.1 Entity слой (Моделът на данни):
- **Цел**: Представя базовите структури на данните, които отговарят на таблиците в базата данни.
- **Реализация**: Класовете в `main.entity` са анотирани с JPA анотации, които дефинират връзките и правилата за работа с базата данни.
- **Основни характеристики**: Връзка с база данни чрез Hibernate.

### 1.2 DTO слой (Data Transfer Objects):
- **Цел**: Представя лек, оптимизиран слой за предаване на данни между различни слоеве (обикновено между DAO и UI/сервизи).
- **Реализация**: Класовете в `main.dto` са опростени обекти, съдържащи само данните, които са необходими за конкретна операция.

### 1.3 Mapper слой:
- **Цел**: Преобразува Entity обекти в DTO обекти и обратно.
- **Реализация**: Класовете в `main.mapper` съдържат статични методи, които конвертират между Entity и DTO.

### 1.4 DAO слой (Data Access Objects):
- **Цел**: Осигурява достъп до базата данни чрез използване на Hibernate.
- **Реализация**: Класовете в `main.dao` съдържат CRUD операции и сложни заявки към базата данни.

### 1.5 HibernateUtil (Конфигурационен слой):
- **Цел**: Осигурява инициализация на `Hibernate SessionFactory`, който управлява връзките с базата данни.

---

## 2. Как работят компонентите заедно

### 2.1 Получаване на данни от потребителя:
- Въвеждат се данни, които съответстват на DTO модела.

### 2.2 Обработка на данни:
- DTO се преобразува в Entity чрез Mapper.
- DAO извършва операции върху Entity обекта в базата данни.

### 2.3 Извличане на данни:
- DAO извлича Entity обекти от базата данни.
- Mapper преобразува Entity обектите обратно в DTO за използването им от потребителя.

### 2.4 Дъмп на базата данни:
- **Файл**: `src/main/resources/transport_company_db.sql`
- Проектът включва дъмп файл на база данни за MySQL, използван в проекта.

---

## 3. Функционалности

### 3.1 Управление на компании
#### Създаване на компания
- **Метод**: `createCompany(CompanyDTO companyDTO)`
- Създава нова компания с име, адрес и начален капитал.

#### Редактиране на компания
- **Метод**: `updateCompany(CompanyDTO companyDTO, Long companyId)`
- Актуализира данни за съществуваща компания.

#### Изтриване на компания
- **Метод**: `deleteCompany(Long id)`
- Маркира компанията като изтрита (логическо изтриване).

#### Преглед на всички компании
- **Метод**: `getAllCompanies()`
- Връща списък на всички активни компании.

#### Справки за компания
- **Метод**: `getCompaniesSortedByRevenue()` - Сортира компаниите по приходи.
- **Метод**: `getCompaniesSortedByName()` - Сортира компаниите по име.

#### Извличане на свързаните данни за компания
- **Метод**: `getCompanyEmployees(Long companyId)` - Извлича служителите.
- **Метод**: `getCompanyClients(Long companyId)` - Извлича клиентите.
- **Метод**: `getCompanyVehicles(Long companyId)` - Извлича превозните средства.

#### Извличане на приходите за компания
- **Метод**: `getTotalRevenueByCompany(Long companyId)` - Извлича приходите на компанията.
- **Метод**: `getTotalRevenueByCompanyInRange(Long companyId, LocalDate startDate, LocalDate endDate)` - Извлича приходите за определен период.

#### Извличане на извършените транспорти за компания
- **Метод**: `getTotalTransportsByCompany(Long companyId)`
- **Метод**: `getTotalTransportsByCompanyInRange(Long companyId, LocalDate startDate, LocalDate endDate)`

#### Създаване на обща справка
- **Метод**: `getCompanyReport(Long companyId)` - Създава справка с цялостната дейност на дадена компания.

---

### 3.2 Управление на служители
#### Създаване на служител
- **Метод**: `saveEmployee(EmployeeDTO employeeDTO, Long companyId)`

#### Редактиране на служител
- **Метод**: `updateEmployee(EmployeeDTO employeeDTO, Long employeeId)`

#### Изтриване на служител
- **Метод**: `deleteEmployee(Long employeeId)`

#### Преглед на всички служители
- **Метод**: `getAllEmployees()`

#### Сортиране на служители
- **Метод**: `getEmployeesSortedBySalary()`

---

### 3.3 Управление на клиенти
#### Създаване на клиент
- **Метод**: `createClient(ClientDTO clientDTO, Long companyId)`

#### Редактиране на клиент
- **Метод**: `updateClient(ClientDTO clientDTO, Long clientId)`

#### Изтриване на клиент
- **Метод**: `deleteClient(Long clientId)`

#### Преглед на всички клиенти
- **Метод**: `getAllClients()`

---

### 3.4 Управление на превозни средства
#### Създаване на превозно средство
- **Метод**: `createVehicle(VehicleDTO vehicleDTO, Long companyId)`

#### Редактиране на превозно средство
- **Метод**: `updateVehicle(VehicleDTO vehicleDTO, Long vehicleId)`

#### Изтриване на превозно средство
- **Метод**: `deleteVehicle(Long vehicleId)`

#### Преглед на всички превозни средства
- **Метод**: `getAllVehicles()`

#### Извличане на превозното средство
- **Метод**: `getVehicleByRegistrationNumber()`

---

### 3.5 Управление на транспортни операции
#### Създаване на транспорт
- **Метод**: `createTransport(TransportDTO transportDTO, Long companyId, Long clientId, Long employeeId, Long vehicleId)`

#### Редактиране на транспорт
- **Метод**: `updateTransport(Long transportId, TransportDTO transportDTO)`

#### Изтриване на транспорт
- **Метод**: `deleteTransport(Long transportId)`

#### Преглед на всички транспорти
- **Метод**: `getAllTransports()`

#### Филтриране на превози по дестинация
- **Метод**: `getTransportsByDestination(String destination)`

---

### 3.6 Справки и отчети
#### Приходи и печалби
- **Метод**: `getTotalRevenueByCompany(Long companyId)`
- **Метод**: `getTotalProfitByCompany(Long companyId)`

#### Генериране на отчет
- **Метод**: `saveCompanyReportToFile(Long companyId, String directoryPath, String startDate, String endDate)`