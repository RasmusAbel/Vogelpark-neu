// HomePage.js
import React from 'react';

class HomePage extends React.Component {
  render() {
    const { textFields, openingHours, LogoUrl } = this.props;
    return (
      <div>
        <img src= {LogoUrl} alt="Home logo" style={{ position: 'absolute', top: '-250px', left: '20px', width: '150px', height: '150px', marginBottom: '200px' }} />
        {Object.entries(textFields).map(([key, value], index) => (
          <div
            key={index}
            style={{ 
              color: "white",
              position: 'absolute', 
              top: `${-300 + index * 50}px`, 
              left: '300px', 
              width: '400px',
              fontSize: '16px', 
            }}
          >
            <div>{key}: {value}</div>
          </div>
        ))}
        {Object.entries(openingHours).map(([weekday, openingHour], index) => (
  <div
    key={index}
    style={{ 
      color: "white",
      position: 'absolute', 
      top: `${-300 + index * 100}px`, 
      left: '900px', 
      width: '400px',
      fontSize: '16px', 
      borderRight: '2px solid #006400', 
      borderBottom: '2px solid #006400',
      borderLeft: '2px solid #006400', 
      borderTop: '2px solid #006400'
    }}
  >
    <div>Wochentag: {openingHour.weekday}</div>
    <div>Startzeit: {openingHour.startTime}</div>
    <div>Endzeit: {openingHour.endTime}</div>
  </div>
))}
      </div>
    );
  }
}

export default HomePage;
