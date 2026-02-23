# Bookstore Modular Monolith — Spring Modulith

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Modulith](https://img.shields.io/badge/Spring%20Modulith-1.1.0-blue.svg)](https://spring.io/projects/spring-modulith)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)

## Overview
This project demonstrates a **modular monolith** architecture using [Spring Modulith](https://spring.io/projects/spring-modulith). It enforces strict module boundaries at build/test time, preventing internal implementation details from leaking across module boundaries.

Spring Modulith enables you to build well-structured, maintainable monoliths that can evolve into microservices if needed, without sacrificing modularity.

---

## 📋 Table of Contents
- [What Makes This Project Special](#what-makes-this-project-special)
- [Module Structure](#module-structure)
- [Implementation Highlights](#implementation-highlights)
- [Spring Modulith Principles](#spring-modulith-principles-applied)
- [Running the Project](#running-the-project)
- [Testing](#testing)
- [Architecture Decisions](#architecture-decisions)
- [Best Practices Followed](#best-practices-followed)

---

## ✨ What Makes This Project Special

This project demonstrates the **correct** way to build a modular monolith using Spring Modulith. It showcases:

### Key Principle
> **Expose a public API from each module. Other modules may only depend on that public API — never on internal implementation classes.**

### Architecture Highlights

| Aspect | Implementation |
|--------|----------------|
| **Public API** | `OrderService` acts as the module's public facade |
| **Encapsulation** | `OrderValidator` hidden in `orders.internal` package |
| **Module Communication** | `InventoryService` → `OrderService` (public API only) |
| **Verification** | Automated tests ensure modularity rules are enforced |

---

## 💡 Implementation Highlights

**OrderService.java** (Public API - Facade Pattern):
```java
package com.nashtechglobal.bookstore.orders;

@Service
public class OrderService {
    private final OrderValidator orderValidator;

    public OrderService(OrderValidator orderValidator) {
        this.orderValidator = orderValidator;
    }

    // Public API method
    public boolean isOrderValid(String orderId) {
        return orderValidator.isValid(orderId);
    }
}
```

**InventoryService.java** (Uses Public API):
```java
package com.nashtechglobal.bookstore.inventory;

import com.nashtechglobal.bookstore.orders.OrderService; // ✅ Public API

@Service
public class InventoryService {
    private final OrderService orderService; // ✅ Depends on public API
    
    public InventoryService(OrderService orderService) {
        this.orderService = orderService;
    }
    
    public boolean canFulfillOrder(String orderId) {
        if (!orderService.isOrderValid(orderId)) {
            throw new IllegalArgumentException("Invalid order ID: " + orderId);
        }
        return true;
    }
}
```

---

## 🏗️ Module Structure

```
com.nashtechglobal.bookstore/
├── Application.java                    ← Spring Boot application entry point
│
├── orders/                             ← MODULE: orders
│   ├── OrderService.java               ← ✅ PUBLIC API (facade pattern)
│   └── internal/                       ← 🔒 INTERNAL (module-private)
│       └── OrderValidator.java         ← Hidden implementation
│
└── inventory/                          ← MODULE: inventory
    └── InventoryService.java           ← Depends ONLY on OrderService
```

### Module Dependencies
```
┌─────────────┐
│  inventory  │
└──────┬──────┘
       │ depends on (public API only)
       ▼
┌─────────────┐
│   orders    │
└─────────────┘
```

### Package Visibility Rules

| Package | Visibility | Can be accessed by |
|---------|-----------|-------------------|
| `com.nashtechglobal.bookstore.orders` | **Public** | All modules |
| `com.nashtechglobal.bookstore.orders.internal` | **Private** | Only `orders` module |
| `com.nashtechglobal.bookstore.inventory` | **Public** | All modules |

---

## 🎯 Spring Modulith Principles Applied

### 1. **Module Detection by Package Convention**
Spring Modulith automatically detects modules by scanning direct subpackages of the base package (`com.nashtechglobal.bookstore`):
- `com.nashtechglobal.bookstore.orders` → **orders** module
- `com.nashtechglobal.bookstore.inventory` → **inventory** module

### 2. **Internal Packages are Private**
Any package named `internal` (or annotated with `@ApplicationModule`) is treated as module-private:
- ✅ `orders` module can access `orders.internal`
- ❌ `inventory` module **cannot** access `orders.internal`

### 3. **Public API by Convention**
Classes in the module's root package are the public API:
- `OrderService` in `orders` package → public API
- `OrderValidator` in `orders.internal` → hidden

### 4. **No Circular Dependencies**
Modules must form a Directed Acyclic Graph (DAG). Spring Modulith prevents:
- `A → B → A` (circular dependency)
- Multiple modules depending on each other

### 5. **Compile-Time Verification**
The modularity structure is verified via tests:
```java
ApplicationModules.of(Application.class).verify();
```

---

## 🚀 Running the Project

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build
```bash
mvn clean install
```

### Run the Application
```bash
mvn spring-boot:run
```

### Run Tests
```bash
mvn test
```

---

## 🧪 Testing

### Modularity Tests
The `ModularityTests` class verifies the modular structure:

```java
@Test
void verifiesModularStructure() {
    modules.verify(); // ✅ Passes if no modularity violations
}
```

**What this test checks:**
- ✅ No module accesses another module's `.internal` package
- ✅ No circular dependencies between modules
- ✅ Module naming conventions are correct
- ✅ All Spring beans respect module boundaries

### Test Output (Success)
```
✅ Module 'orders' verified successfully
✅ Module 'inventory' verified successfully
✅ No modularity violations found
```

### Test Output (Failure - if internal class accessed)
```
❌ Module 'inventory' violates boundaries:
   - InventoryService accesses OrderValidator from orders.internal
```

---

## 🏛️ Architecture Decisions

### 1. **Facade Pattern for Public APIs**
Each module exposes a single entry point (`OrderService`) that delegates to internal components. This:
- Simplifies the public API
- Allows internal refactoring without breaking consumers
- Follows the Dependency Inversion Principle

### 2. **Package-Based Module Organization**
Instead of separate JAR files, modules are organized by packages. This:
- Keeps the codebase in a single repository
- Enables easy refactoring
- Allows gradual migration to microservices if needed

### 3. **Test-Driven Modularity**
The `modules.verify()` test acts as a "compiler" for modularity rules:
- Catches violations early in CI/CD pipeline
- Prevents accidental coupling
- Documents intended architecture

---

## ✨ Best Practices Followed

### ✅ 1. Clear Module Boundaries
Each module has a well-defined responsibility:
- **orders**: Order validation and management
- **inventory**: Inventory fulfillment

### ✅ 2. Minimal Public API Surface
Only necessary classes are exposed as public API:
- `OrderService` is public
- `OrderValidator` remains internal

### ✅ 3. Stable Interfaces
Public APIs use stable, well-documented interfaces that change infrequently.

### ✅ 4. Explicit Dependencies
Dependencies between modules are explicit and unidirectional:
```
inventory → orders (one-way)
```

### ✅ 5. Documentation as Code
The module structure is documented via:
- Spring Modulith's `Documenter` (generates PlantUML diagrams)
- Comprehensive README
- Inline code comments

### ✅ 6. Test Coverage
Automated tests ensure:
- Modularity rules are enforced
- Public APIs work correctly
- Internal classes remain hidden

---

## 📊 Generated Documentation

After running tests, Spring Modulith generates documentation:

```bash
target/spring-modulith-docs/
├── all-modules.puml           # Complete module diagram
├── module-orders.puml         # orders module details
└── module-inventory.puml      # inventory module details
```

Render these diagrams at [plantuml.com](https://www.plantuml.com/plantuml/uml/).

---

## 🔄 Future Enhancements

1. **Event-Driven Communication**: Use Spring Modulith's event publication registry
2. **Module Observability**: Add module-level metrics and tracing
3. **API Versioning**: Version public APIs for backward compatibility
4. **Integration Tests**: Test cross-module interactions
5. **Migration Path**: Document how to extract modules into microservices

---

## 📚 References

- [Spring Modulith Documentation](https://docs.spring.io/spring-modulith/docs/current/reference/html/)
- [Modular Monolith Architecture](https://www.kamilgrzybek.com/blog/posts/modular-monolith-primer)
- [Domain-Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html)

---

## 👤 Author

**NashTech Global**

For questions or feedback, please open an issue in the repository.

---

## 📄 License

This project is created for educational purposes.