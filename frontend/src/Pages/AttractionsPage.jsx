import React from 'react';

class AttractionsPage extends React.Component {
  state = {
    attractions: [
      {
        id: 1,
        name: 'Attraktion1',
        description: 'Beschreibung der Attraktion1... sdfsfdsdf fsdfsdfsf sdfsfsdfs dsf sfsf sdfs  sfsdfsd',
        tags: ['Tag1', 'Tag2', 'Tag3'],
        logo: 'https://media.discordapp.net/attachments/701866260803223573/1068667665494839427/dfghjdjdjdgh.gif?ex=65eb9952&is=65d92452&hm=32bbbea1370d9e7b535fe8d995fd5bba2eaeb01859ab8b96afb52e1ea0b320f4&'
      },
      {
        id: 2,
        name: 'Attraktion2',
        description: 'Beschreibung der Attraktion2... dsfsdf sdfsdf sdfsdf sdfsd fsdfsdf',
        tags: ['Tag4', 'Tag5', 'Tag6'],
        logo: 'https://media.discordapp.net/attachments/709410500290674730/1066066941661425715/hdfjghkjkg.gif?ex=65eb5db5&is=65d8e8b5&hm=2f88bae7a01323517cc58175ca1ffc7e3c13b034ac2a7ae5b48145b0d3b30fb4&'
      },
      {
        id: 3,
        name: 'Attraktion3',
        description: 'Beschreibung der Attraktion3... dsfsdf sdfsdf sdfsdf sdfsd fsdfsdf',
        tags: ['Tag7', 'Tag8', 'Tag9'],
        logo: 'https://c.tenor.com/Kp3Q2HiVUFQAAAAM/cat-on-fire-sitting.gif'
      },
      {
        id: 4,
        name: 'Attraktion4',
        description: 'Beschreibung der Attraktion4... dsfsdf sdfsdf sdfsdf sdfsd fsdfsdf',
        tags: ['Tag10', 'Tag11', 'Tag12'],
        logo: 'https://media.discordapp.net/attachments/663381537089388544/780457971854475294/giphy.gif?ex=65ef2620&is=65dcb120&hm=c1cb107ef4a3f2a0428a0cd2b1c5befd03b834f70f4041294c3de69590bb0518&'
      }
    ]
  };

  render() {

    return (
      <div>
        <p style={{ position: 'absolute', top: '-350px', left: '20px', fontSize: '24px', color: '#FFFFFF' }}>Attraktionen</p>
        <div style={{ position: 'fixed', top: '25%', transform: 'translateY(-50%)', marginLeft: '20vw', marginRight: '20vw', marginTop: '300px', maxHeight: '70vh', overflowY: 'auto' }}>
          {this.state.attractions.map(attraction => (
            <div key={attraction.id} style={{ border: '2px solid #006400', marginBottom: '20px' }}>
              <div style={{ display: 'flex' }}>
                <div style={{ width: '200px', height: '200px', borderRight: '2px solid #006400', borderBottom: '2px solid #006400', padding: '10px' }}>
                  {/* Hier das Logo */}
                  <img src={attraction.logo} alt="Logo" style={{ width: '100%', height: '100%' }} />
                </div>
                <div style={{ padding: '10px' }}>
                  {/* Hier der Attraktionsname und die Beschreibung */}
                  <p style={{ fontWeight: 'bold', textDecoration: 'underline' }}>{attraction.name}</p>
                  <p>{attraction.description}</p>
                </div>
              </div>
              <div style={{ textAlign: 'center', borderTop: '2px solid #006400', padding: '10px' }}>
                {/* Hier die Tags als Buttons */}
                {attraction.tags.map(tag => (
                  <button key={tag}>{tag}</button>
                ))}
              </div>
            </div>
          ))}
        </div>
      </div>
    );
  }
}

export default AttractionsPage;
