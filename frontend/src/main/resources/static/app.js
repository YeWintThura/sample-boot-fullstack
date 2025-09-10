$(document).ready(function() {
    let currentConcertId = null;
    let selectedSeatId = null;
    
    // Backend API base URL - assuming backend runs on port 8081
    const API_BASE_URL = 'http://localhost:8081';

    // Load concerts when page loads
    loadConcerts();

    // Load concerts from backend
    function loadConcerts() {
        $.ajax({
            url: `${API_BASE_URL}/concerts`,
            method: 'GET',
            dataType: 'json',
            success: function(concerts) {
                displayConcerts(concerts);
            },
            error: function(xhr, status, error) {
                console.error('Error loading concerts:', error);
                $('#concerts-list').html('<p class="error">Error loading concerts. Please make sure the backend is running on port 8081.</p>');
            }
        });
    }

    // Display concerts in the UI
    function displayConcerts(concerts) {
        const concertsList = $('#concerts-list');
        concertsList.empty();

        if (concerts.length === 0) {
            concertsList.html('<p>No concerts available.</p>');
            return;
        }

        concerts.forEach(function(concert) {
            const concertCard = $(`
                <div class="concert-card" data-concert-id="${concert.id}">
                    <h3>${concert.title}</h3>
                    <p><strong>Performer:</strong> ${concert.performer}</p>
                    <p><strong>Date:</strong> ${formatDate(concert.date)}</p>
                    <button class="btn btn-primary view-seats-btn" data-concert-id="${concert.id}">
                        View Seats
                    </button>
                </div>
            `);
            concertsList.append(concertCard);
        });
    }

    // Format date for display
    function formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    }

    // Load seats for a specific concert
    function loadSeats(concertId) {
        $.ajax({
            url: `${API_BASE_URL}/concerts/${concertId}/seats`,
            method: 'GET',
            dataType: 'json',
            success: function(seats) {
                displaySeats(seats);
                currentConcertId = concertId;
                $('#concerts-section').hide();
                $('#seats-section').show();
            },
            error: function(xhr, status, error) {
                console.error('Error loading seats:', error);
                alert('Error loading seats. Please try again.');
            }
        });
    }

    // Display seats in the UI
    function displaySeats(seats) {
        const seatsContainer = $('#seats-container');
        seatsContainer.empty();

        // Group seats by zone
        const seatsByZone = {};
        seats.forEach(function(seat) {
            if (!seatsByZone[seat.zone]) {
                seatsByZone[seat.zone] = [];
            }
            seatsByZone[seat.zone].push(seat);
        });

        // Create seat layout for each zone
        Object.keys(seatsByZone).forEach(function(zone) {
            const zoneDiv = $(`<div class="zone-section"><h4>${zone} Zone</h4><div class="seats-grid"></div></div>`);
            const seatsGrid = zoneDiv.find('.seats-grid');

            seatsByZone[zone].forEach(function(seat) {
                const seatClass = seat.booked ? 'seat booked' : 'seat available';
                const seatButton = $(`
                    <button class="${seatClass}" 
                            data-seat-id="${seat.id}" 
                            data-seat-number="${seat.number}"
                            data-zone="${seat.zone}"
                            ${seat.booked ? 'disabled' : ''}>
                        ${seat.number}
                        ${seat.booked ? '<br><small>Booked</small>' : ''}
                    </button>
                `);
                seatsGrid.append(seatButton);
            });

            seatsContainer.append(zoneDiv);
        });
    }

    // Event handlers
    $(document).on('click', '.view-seats-btn', function() {
        const concertId = $(this).data('concert-id');
        loadSeats(concertId);
    });

    $(document).on('click', '.seat.available', function() {
        selectedSeatId = $(this).data('seat-id');
        const seatNumber = $(this).data('seat-number');
        const zone = $(this).data('zone');
        
        $('#selected-seat-info').text(`${zone} Zone - Seat ${seatNumber}`);
        $('#booking-modal').show();
    });

    $(document).on('click', '#back-to-concerts', function() {
        $('#seats-section').hide();
        $('#concerts-section').show();
        currentConcertId = null;
        selectedSeatId = null;
    });

    $(document).on('click', '.close, #cancel-booking', function() {
        $('#booking-modal').hide();
        selectedSeatId = null;
    });

    // Handle booking form submission
    $('#booking-form').on('submit', function(e) {
        e.preventDefault();
        
        if (!selectedSeatId || !currentConcertId) {
            alert('Please select a seat first.');
            return;
        }

        const attendeeName = $('#attendee-name').val().trim();
        if (!attendeeName) {
            alert('Please enter your name.');
            return;
        }

        // Book the seat
        $.ajax({
            url: `${API_BASE_URL}/concerts/${currentConcertId}/seats/${selectedSeatId}/book`,
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                attendee: attendeeName
            }),
            success: function(response) {
                alert('Seat booked successfully!');
                $('#booking-modal').hide();
                $('#attendee-name').val('');
                selectedSeatId = null;
                // Reload seats to show updated status
                loadSeats(currentConcertId);
            },
            error: function(xhr, status, error) {
                console.error('Error booking seat:', error);
                if (xhr.status === 404) {
                    alert('Seat or concert not found.');
                } else {
                    alert('Error booking seat. Please try again.');
                }
            }
        });
    });

    // Close modal when clicking outside
    $(window).on('click', function(e) {
        if (e.target.id === 'booking-modal') {
            $('#booking-modal').hide();
            selectedSeatId = null;
        }
    });
});
