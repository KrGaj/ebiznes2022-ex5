const server = require("./../config/config.json")

describe("POST cart", () => {
    it("Add product to cart", () => {
        cy.request("POST", server.serverURL + "cart", {
            user: Cypress.env("userId"),
            amount: "2",
            product: Cypress.env("productId")
        })
            .then((response) => {
                expect(response.status).to.eq(200)
                expect(response.body).to.have.property("cartProductId")

                Cypress.env("cartProductId", response.body.cartProductId)
            })
    })
})

describe("GET cart", () => {
    it("Get cart by user ID", () => {
        cy.request("GET", server.serverURL + "cart", {
            user_id: Cypress.env("userId")
        })
            .then((response) => {
                expect(response.body.status).to.eq(200)
                expect(response.body[0]).to.have.property("id")
                expect(response.body[0]).to.have.property("user")
                expect(response.body[0]).to.have.property("amount")
                expect(response.body[0]).to.have.property("product")

                expect(response.body[0].id).to.eq(Cypress.env("cartProductId"))
                expect(response.body[0].user).to.eq(Cypress.env("userId"))
                expect(response.body[0].amount).to.eq(2)
                expect(response.body[0].product).to.eq(Cypress.env("productId"))
            })
    })
})

describe("PUT cart product", () => {
    it("Update number of products in cart", () => {
        cy.request("PUT", server.serverURL + "cart", {
            id: Cypress.env("cartProductId"),
            amount: "3"
        })
            .then((response) => {
                expect(response.status).to.eq(200)
                expect(response.body).to.have.property("updated")

                expect(response.body.updated).to.eq(1)
            })
    })
})

describe("DELETE cart", () => {
    it("Remove product from cart", () => {
        cy.request("DELETE", server.serverURL + "cart", {
            id: Cypress.env("cartProductId")
        })
            .then((response) => {
                expect(response.body).to.eq(200)
                expect(response.body).to.have.property("removed")

                expect(response.body.removed).to.eq(1)
            })
    })
})
