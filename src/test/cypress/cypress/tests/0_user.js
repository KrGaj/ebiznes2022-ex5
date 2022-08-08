const server = require("./../config/config.json")
const users = require("./../fixtures/user.json")

describe('POST user', () => {
    it("Add user", () => {
        cy.request("POST", server.serverURL + "users", users[0])
            .then((response) => {
                expect(response.status).to.eq(200)
                expect(response.body).to.have.property("userId")

                Cypress.env('userId', response.body.userId)
            })
    })

    it("Duplicate user", () => {
        cy.request("POST", server.serverURL + "users", users[0])
            .then((response) => {
                expect(response.status).to.eq(500)
            })
    })
});

describe("GET user", () => {
    it("GET user by ID", () => {
        cy.request("GET", server.serverURL + "users?user_id=" + Cypress.env("userId"))
            .then((response) => {
                expect(response.status).to.eq(200)
                expect(response.body).to.have.property("id")
                expect(response.body).to.have.property("email")
                expect(response.body).to.have.property("passwordAsHash")
                expect(response.body).to.have.property("accessToken")

                expect(response.body.id).to.eq(users[0].username)
                expect(response.body.email).to.eq(users[0].email)
                expect(response.body.passwordAsHash).to.eq(users[0].passwordAsHash)
                expect(response.body.accessToken).to.eq(users[0].accessToken)
            })
    })
})
