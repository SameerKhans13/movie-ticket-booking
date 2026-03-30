# Class Diagram - Movie Ticket Booking System

## System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│              MOVIE TICKET BOOKING SYSTEM (Facade)               │
│  - Creates and manages theaters, shows, and bookings           │
│  - Handles seat selection and payment processing              │
│  - Manages user authentication and authorization              │
└──────────────────┬──────────────────────────────────────────────┘
                   │
         ┌─────────┼──────────┬──────────────┐
         ▼         ▼          ▼              ▼
    ┌────────┐ ┌────────┐ ┌──────────────┐ ┌─────────────┐
    │ THEATER│ │ SHOW   │ │BOOKING       │ │PAYMENT      │
    │        │ │        │ │              │ │             │
    │ +Audit │ │+Pricing│ │+Seats        │ │+Methods     │
    │ aloria │ │Strategy│ │+Customer     │ │+Processing  │
    │ ms     │ │        │ │+Status       │ │+Refunds     │
    └────────┘ └────────┘ └──────────────┘ └─────────────┘
        ▲         ▲              ▲              ▲
        │         │              │              │
        └─────────┼──────────────┼──────────────┘
                  │              │
         ┌────────┴──────┐       │
         ▼               ▼       ▼
    ┌──────────────┐ ┌──────────────┐
    │  AUDITORIUM  │ │  SEAT        │
    │ - seats[]    │ │ - seatType   │
    │ - shows[]    │ │ - available  │
    │ - type       │ │ - locked     │
    └──────────────┘ └──────────────┘
        ▲
        │
    ┌───┴────────┐
    │            │
┌───┴──────┐ ┌──┴────────┐
│ CUSTOMER │ │ADMIN      │
│ - User   │ │ - User    │
│ - Books  │ │ - Privs   │
└──────────┘ └───────────┘
```

## Class Relationships

### 1. **MovieTicketBookingSystem** (Facade Pattern)
- **Purpose**: Single entry point for the entire system
- **Responsibilities**:
  - Create and manage theaters
  - Handle movie and show management
  - Manage customer bookings
  - Coordinate payment processing

```java
public class MovieTicketBookingSystem {
    - theaters: List<Theater>
    - bookingService: BookingService
    - paymentService: PaymentService
    - authService: AuthenticationService
    
    + addTheater(theater)
    + addMovie(movie)
    + createShow(show)
    + searchShows(movie, date)
    + bookSeats(customer, show, seats)
    + processPayment(booking, paymentMethod)
    + cancelBooking(bookingId)
    + getSystemStatus()
}
```

### 2. **Theater**
- **Purpose**: Represents a physical theater location
- **Responsibilities**:
  - Manage auditoriums
  - Track theater information
  - Organize shows across auditoriums

```java
public class Theater {
    - theaterId: String
    - name: String
    - address: String
    - city: String
    - auditoriums: Map<String, Auditorium>
    
    + addAuditorium(auditorium)
    + getAuditorium(auditoriumId)
    + getAllAuditoriums()
    + getShowsByMovie(movieId)
}
```

### 3. **Auditorium**
- **Purpose**: Represents individual theater screens
- **Responsibilities**:
  - Manage seats
  - Track shows
  - Prevent show overlaps
  - Return available seats

```java
public class Auditorium {
    - auditoriumId: String
    - name: String
    - screenType: String
    - seats: Map<String, Seat>
    - shows: List<Show>
    
    + addSeat(seat)
    + addShow(show)
    + getAvailableSeats(show)
    + checkShowOverlap(show)
    + getSeat(seatId)
}
```

### 4. **Show** (Strategy Pattern)
- **Purpose**: Represents a movie showing
- **Responsibilities**:
  - Track show details and timing
  - Calculate ticket prices using strategy
  - Manage pricing strategy

```java
public class Show {
    - showId: String
    - movie: Movie
    - auditorium: Auditorium
    - startTime: long
    - endTime: long
    - basePrice: double
    - pricingStrategy: PricingStrategy
    
    + getTicketPrice(seatType)
    + setPricingStrategy(strategy)
    + isShowRunning()
    + getAvailableSeats()
}
```

### 5. **Seat**
- **Purpose**: Represents individual theater seats
- **Responsibilities**:
  - Track seat state and availability
  - Manage seat type and pricing
  - Handle thread-safe booking

```java
public class Seat {
    - seatId: String
    - seatType: SeatType
    - row: char
    - seatNumber: int
    - show: Show
    - isAvailable: boolean
    
    + bookSeat()
    + releaseSeat()
    + isAvailable()
    + getSeatType()
    + getSeatId()
}
```

### 6. **Booking**
- **Purpose**: Represents customer reservations
- **Responsibilities**:
  - Store booking details
  - Track booking status
  - Calculate total price

```java
public class Booking {
    - bookingId: String
    - customer: Customer
    - seats: List<Seat>
    - show: Show
    - bookingTime: long
    - status: BookingStatus
    - totalPrice: double
    - payment: Payment
    
    + calculateTotalPrice()
    + confirmBooking()
    + cancelBooking()
    + getBookingDetails()
}
```

### 7. **Payment**
- **Purpose**: Manages payment transactions
- **Responsibilities**:
  - Process payments using selected method
  - Track payment status
  - Handle refunds

```java
public class Payment {
    - paymentId: String
    - amount: double
    - method: PaymentMethod
    - status: PaymentStatus
    - transactionTime: long
    
    + processPayment()
    + setPaymentMethod(method)
    + refundPayment()
    + getPaymentStatus()
}
```

### 8. **User Hierarchy**
- **Purpose**: Represent different user types
- **Relationships**:

```java
public abstract class User {
    - userId: String
    - name: String
    - email: String
    - phone: String
    
    + getUserId()
    + getName()
    + getEmail()
}

public class Customer extends User {
    - bookings: List<Booking>
    
    + addBooking(booking)
    + getBookings()
}

public class Admin extends User {
    - privileges: Set<String>
    
    + hasPrivilege(privilege)
    + addMovie(movie)
    + addTheater(theater)
    + updatePricing(show, strategy)
}
```

### 9. **Movie**
- **Purpose**: Stores movie information
- **Responsibilities**:
  - Track movie details
  - Return movie information

```java
public class Movie {
    - movieId: String
    - title: String
    - duration: int
    - genre: String
    
    + getMovieId()
    + getTitle()
    + getDuration()
}
```

### 10. **Services Layer**

#### **BookingService** (Business Logic)
```java
public class BookingService {
    - bookings: Map<String, Booking>
    - seatService: SeatService
    
    + createBooking(customer, show, seats)
    + confirmBooking(booking, payment)
    + cancelBooking(bookingId)
    + getBooking(bookingId)
}
```

#### **SeatService** (Concurrency Control)
```java
public class SeatService {
    - seatLocks: Map<String, Long>
    - LOCK_TIMEOUT: long = 5 * 60 * 1000 // 5 minutes
    
    + lockSeat(seat)
    + unlockSeat(seat)
    + isSeatLocked(seat)
}
```

#### **ShowService** (Show Management)
```java
public class ShowService {
    - shows: Map<String, Show>
    
    + createShow(movieId, auditoriumId, startTime, endTime, basePrice)
    + getShowsByMovie(movieId)
    + updateShowPricing(show, strategy)
}
```

#### **AuthenticationService** (Access Control)
```java
public class AuthenticationService {
    
    + authenticate(user, password)
    + authorize(user, action)
    + isAdmin(user)
}
```

## Enums

### **SeatType**
- SILVER (1.0x multiplier) → ₹500
- GOLD (1.5x multiplier) → ₹750
- PLATINUM (2.0x multiplier) → ₹1000
- VIP (3.0x multiplier) → ₹1500

### **BookingStatus**
- PENDING: Awaiting payment
- CONFIRMED: Payment received
- CANCELLED: Booking cancelled
- EXPIRED: Hold time expired

### **PaymentStatus**
- INITIATED: Payment started
- SUCCESS: Payment successful
- FAILED: Payment failed
- REFUNDED: Payment refunded

### **Direction**
- UP: Searching upward
- DOWN: Searching downward

## Design Patterns Used

### 1. **Facade Pattern** ⭐
- `MovieTicketBookingSystem` class
- Simplifies complex interaction between theaters, shows, bookings, payments
- Single point of entry for client applications
```java
// Complex operations become simple
system.bookSeats(customer, show, selectedSeats);
system.processPayment(booking, creditCard);
system.cancelBooking(bookingId);
```

### 2. **Strategy Pattern** ⭐⭐
- `PricingStrategy` interface for dynamic pricing
- `PaymentMethod` interface for payment processing
- Runtime selection of algorithms

```java
// Pricing strategies
show.setPricingStrategy(new StandardPricing());      // 1.0x
show.setPricingStrategy(new WeekendSurge());        // 1.2x
show.setPricingStrategy(new HolidayPremium());      // 1.5x

// Payment methods
payment.setPaymentMethod(new CreditCardPayment()); 
payment.setPaymentMethod(new DebitCardPayment());
```

### 3. **Proxy Pattern**
- `AuthenticationService` controls access
- Role-based authorization for admin operations
- Prevents unauthorized access

```java
authService.authorize(user, "ADD_MOVIE");
authService.authorize(user, "UPDATE_PRICING");
```

### 4. **Repository Pattern** (Foundation)
- Services manage data access
- Abstracting persistence layer
- Easy to switch to database

```java
BookingService manages bookings
ShowService manages shows
SeatService manages seat states
```

### 5. **Singleton Pattern** (Can be extended)
- Services can be singletons
- Ensures single instance of system state

## Interaction Flow

### Booking Request Flow:
```
Customer searches for show
        ↓
System.searchShows(movie, date)
        ↓
Return list of available shows
        ↓
Customer views available seats
        ↓
Auditorium.getAvailableSeats(show)
        ↓
Customer selects seats
        ↓
SeatService.lockSeat() ← 5-min timeout
        ↓
Create Booking (PENDING)
        ↓
Select payment method
        ↓
Payment.processPayment() ← Strategy Pattern
        ↓
Payment SUCCESS / FAILED
        ├─ SUCCESS → Seat.bookSeat()
        │            Booking.confirmBooking() (CONFIRMED)
        └─ FAILED  → SeatService.unlockSeat()
                     Release reservation
```

### Show Overlap Prevention Flow:
```
Admin creates show
        ↓
ShowService.createShow()
        ↓
Auditorium.addShow()
        ↓
Auditorium.checkShowOverlap()
        ├─ Overlap detected → Exception thrown
        └─ No overlap → Show added
```

### Pricing Calculation Flow:
```
Customer books seats
        ↓
For each seat:
    Show.getTicketPrice(seatType)
        ↓
    BasePrice × SeatType.multiplier
        ↓
    × PricingStrategy.getPrice()
        ↓
    = Final seat price
        ↓
Sum all seat prices = Total booking price
```

## Concurrency Control

### Thread-Safe Seat Booking:
```
Timeline:
T0: Customer A selects seat X
    ├─ SeatService.lockSeat(X)
    ├─ Lock: 5 minutes
    └─ Seat marked LOCKED
    
T1: Customer B tries seat X
    ├─ SeatService.isSeatLocked(X)
    ├─ Lock exists
    └─ Access DENIED
    
T2: Customer A payment processing
    
T4: Payment SUCCESS
    ├─ Seat.bookSeat()
    ├─ isAvailable = false
    └─ Permanently booked
    
T5: Lock expires (if no payment)
    ├─ SeatService.unlockSeat(X)
    ├─ Seat marked AVAILABLE
    └─ Seat released
    
T6: Customer B can book seat X ✓
```

## Scalability & Extension Points

### 1. **Pricing Strategies** ✅
Current: Standard, Weekend, Holiday
```java
// Easy to add:
public class EarlyBirdDiscount implements PricingStrategy { }
public class DynamicPricingByOccupancy implements PricingStrategy { }
public class FestivalSale implements PricingStrategy { }
```

### 2. **Payment Methods** ✅
Current: Credit Card, Debit Card
```java
// Easy to add:
public class WalletPayment implements PaymentMethod { }
public class UPIPayment implements PaymentMethod { }
public class BankTransfer implements PaymentMethod { }
```

### 3. **Seat Types** ✅
Current: Silver, Gold, Platinum, VIP
```java
// Easy to add to enum:
RECLINERS(4.0),
COUPLE_SEATS(5.0),
WHEELCHAIR_ACCESSIBLE(1.0)
```

### 4. **Booking Features**
- [ ] Combo offers (movie + food)
- [ ] Group booking discounts
- [ ] Subscription/membership plans
- [ ] Loyalty points system
- [ ] Cancellation policies

### 5. **System Features**
- [ ] Multi-building support
- [ ] Email notifications
- [ ] SMS alerts
- [ ] Analytics dashboard
- [ ] Reporting system

### 6. **Advanced Scheduling**
- [ ] Machine learning for demand prediction
- [ ] AI-based dynamic pricing
- [ ] Intelligent seat recommendation

### 7. **Database Integration**
```java
// Replace in-memory with:
public class DatabaseBookingRepository implements BookingRepository { }
public class DatabaseSeatRepository implements SeatRepository { }
```

### 8. **API Integration**
- [ ] REST API endpoints
- [ ] Third-party payment gateways
- [ ] Email service integration
- [ ] SMS service integration

## Code Organization Summary

```
Package Structure:
├── models/               (Entities)
│   ├── User, Customer, Admin
│   ├── Theater, Auditorium, Show
│   ├── Seat, Booking, Payment
│   └── Movie
│
├── enums/                (Type definitions)
│   ├── SeatType
│   ├── BookingStatus
│   ├── PaymentStatus
│   └── Direction
│
├── interfaces/           (Contracts)
│   ├── PaymentMethod
│   └── PricingStrategy
│
├── implementations/      (Concrete strategies)
│   ├── CreditCardPayment, DebitCardPayment
│   ├── StandardPricing, WeekendSurge, HolidayPremium
│   └── (Future payment & pricing)
│
├── services/             (Business logic)
│   ├── BookingService
│   ├── SeatService
│   ├── ShowService
│   └── AuthenticationService
│
├── exceptions/           (Error handling)
│   ├── SeatAlreadyBookedException
│   ├── InvalidShowException
│   └── UnauthorizedAccessException
│
└── MovieTicketBookingSystem.java (Facade)
```

## Key Metrics

| Metric | Value |
|--------|-------|
| Core Classes | 10 |
| Service Classes | 4 |
| Enum Types | 4 |
| Interfaces | 2 |
| Design Patterns | 5 |
| SOLID Principles | 5/5 |
| Concurrency Safe | ✅ Yes |
| Extensible | ✅ Yes |
| Thread-Safe | ✅ Yes |

---

This comprehensive class diagram provides a complete blueprint for the Movie Ticket Booking System with clear relationships, design patterns, and extension points for future enhancements.
