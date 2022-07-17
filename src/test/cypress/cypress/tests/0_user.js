const server = require("./../config/config.json")
const users = require("./../fixtures/user.json")

describe('POST user', () => {
    it("Add user", () => {
        cy.request("POST", server.serverURL + "users", users[0])
            .then((response) => {
                expect(response.status).to.eq(200)
                expect(response.body).to.have.property("userId")
                expect(response.body).to.have.property("cartId")

                Cypress.env('userId', response.body.userId)
                Cypress.env('cartId', response.body.cartId)
            })
    })
});