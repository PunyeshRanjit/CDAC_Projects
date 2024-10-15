using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace DestinaFinal.Migrations
{
    /// <inheritdoc />
    public partial class notificationfieldnamerelationshipupdated : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Notifications_Users_FromAgentId",
                table: "Notifications");

            migrationBuilder.DropForeignKey(
                name: "FK_Notifications_Users_ToCustomerId",
                table: "Notifications");

            migrationBuilder.RenameColumn(
                name: "ToCustomerId",
                table: "Notifications",
                newName: "FromId");

            migrationBuilder.RenameColumn(
                name: "FromAgentId",
                table: "Notifications",
                newName: "CustomerId");

            migrationBuilder.RenameIndex(
                name: "IX_Notifications_ToCustomerId",
                table: "Notifications",
                newName: "IX_Notifications_FromId");

            migrationBuilder.RenameIndex(
                name: "IX_Notifications_FromAgentId",
                table: "Notifications",
                newName: "IX_Notifications_CustomerId");

            migrationBuilder.AddForeignKey(
                name: "FK_Notifications_Users_CustomerId",
                table: "Notifications",
                column: "CustomerId",
                principalTable: "Users",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_Notifications_Users_FromId",
                table: "Notifications",
                column: "FromId",
                principalTable: "Users",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Notifications_Users_CustomerId",
                table: "Notifications");

            migrationBuilder.DropForeignKey(
                name: "FK_Notifications_Users_FromId",
                table: "Notifications");

            migrationBuilder.RenameColumn(
                name: "FromId",
                table: "Notifications",
                newName: "ToCustomerId");

            migrationBuilder.RenameColumn(
                name: "CustomerId",
                table: "Notifications",
                newName: "FromAgentId");

            migrationBuilder.RenameIndex(
                name: "IX_Notifications_FromId",
                table: "Notifications",
                newName: "IX_Notifications_ToCustomerId");

            migrationBuilder.RenameIndex(
                name: "IX_Notifications_CustomerId",
                table: "Notifications",
                newName: "IX_Notifications_FromAgentId");

            migrationBuilder.AddForeignKey(
                name: "FK_Notifications_Users_FromAgentId",
                table: "Notifications",
                column: "FromAgentId",
                principalTable: "Users",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);

            migrationBuilder.AddForeignKey(
                name: "FK_Notifications_Users_ToCustomerId",
                table: "Notifications",
                column: "ToCustomerId",
                principalTable: "Users",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }
    }
}
