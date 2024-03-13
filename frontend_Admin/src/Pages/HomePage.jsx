import React from 'react';

class HomePage extends React.Component {
  render() {
    const { textFields } = this.props;
    return (
      <div>
        <img src="https://cdn.discordapp.com/attachments/723252229317853284/1212009496168108062/image.png?ex=65f046cd&is=65ddd1cd&hm=d7776bfae439f1f98d46ba8b03e2f91f172fc6262982f08e59553d56ba6cf918&" alt="Home logo" style={{ position: 'absolute', top: '-250px', left: '20px', width: '150px', height: '150px', marginBottom: '200px' }} />
        {Object.entries(textFields).map(([key, value], index) => (
          <div
            key={index}
            style={{ 
              position: 'absolute', 
              top: `${50 + index * 50}px`, 
              left: '500px', 
              width: '400px', // Hier ändere die Breite des Textfeld-Containers
              fontSize: '16px', 
              color: 'black' 
            }}
          >
            <div>{key}: {value}</div>
          </div>
        ))}
      </div>
    );
  }
}

export default HomePage;
